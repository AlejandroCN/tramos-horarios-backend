package com.horarios.backend.restcontrollers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.horarios.backend.model.entity.Rol;
import com.horarios.backend.model.entity.Usuario;
import com.horarios.backend.services.UsuarioService;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioRestController {
	
	@Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("")
    public ResponseEntity<?> save(@Valid @RequestBody Usuario usuario, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Usuario usuarioCreado = null;
        Usuario usuarioExistente = null;

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors()
                    .stream()
                    .map((error) -> "Error en campo: " + error.getField() + ", error: " + error.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<List<String>>(errores, HttpStatus.BAD_REQUEST);
        }

        try {
        	usuarioExistente = this.usuarioService.findByEmail(usuario.getEmail());
        	if (usuarioExistente != null) {
        		response.put("mensaje", "El correo electrónico indicado ya está en uso");
        		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        	}
        	
        	usuario.setRol(new Rol());
        	usuario.getRol().setId(1L);
            usuario.setPassword(this.passwordEncoder.encode(usuario.getPassword()));
            usuario.setEnabled(true);
            usuario.setSignUpWithGoogle(false);
            usuarioCreado = this.usuarioService.save(usuario);
        } catch(DataAccessException ex) {
            response.put("mensaje", "Ocurrió un error interno al tratar de persistir usuario");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        usuarioCreado.setPassword("");
        return new ResponseEntity<Usuario>(usuarioCreado, HttpStatus.CREATED);
    }
    
    @PutMapping("/updateHorarios")
    @Secured("ROLE_USUARIO")
    public ResponseEntity<?> updateHorarios(@RequestBody Usuario usuario) {
    	Map<String, Object> response = new HashMap<>();
        Usuario usuarioExistente = null;
        Usuario usuarioActualizado = null;
        
        try {
        	usuarioExistente = this.usuarioService.findById(usuario.getId());
        	usuarioExistente.setHorarios(usuario.getHorarios());
        	usuarioActualizado = this.usuarioService.save(usuarioExistente);
        } catch (DataAccessException ex) {
        	response.put("mensaje", "Ocurrió un error interno al tratar de recuperar el usuario solicitado");
            response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        usuarioActualizado.setPassword("");
        return new ResponseEntity<Usuario>(usuarioActualizado, HttpStatus.CREATED); 
    }

}
