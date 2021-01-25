package com.horarios.backend.restcontrollers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.horarios.backend.model.entity.Horario;
import com.horarios.backend.services.HorarioService;

@RestController
@RequestMapping("api/horarios")
public class HorarioRestController {
	
	@Autowired
	private HorarioService horarioService;
	
	@PostMapping("/reservarHorario/{id}")
	public ResponseEntity<?> reservarHorario(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<String, Object>();
		Horario horarioExistente = null;
		
		try {
			horarioExistente = this.horarioService.findById(id);
			if (horarioExistente == null) {
				response.put("mensaje", "El horario solicitado no existe!");
				return new ResponseEntity<Map <String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			
			horarioExistente.setContadorReservaciones(horarioExistente.getContadorReservaciones() + 1);
			this.horarioService.save(horarioExistente);
		} catch (DataAccessException ex) {
			response.put("mensaje", "Ocurrió un error interno al recuperar el horario solicitado");
			response.put("error", ex.getMessage() + ": " + ex.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map <String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return ResponseEntity.ok(horarioExistente);
	}
	
	@GetMapping("")
	@Secured("ROLE_USUARIO")
	public ResponseEntity<?> findAll() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<Horario> horarios = null;
		
		try {
			horarios = this.horarioService.findAll();
		} catch (DataAccessException ex) {
			response.put("mensaje", "Ocurrió un error interno al recuperar los horarios");
			response.put("error", ex.getMessage() + ": " + ex.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map <String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return ResponseEntity.ok(horarios);
	}

}
