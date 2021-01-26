package com.horarios.backend.restcontrollers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.horarios.backend.auth.service.JWTService;
import com.horarios.backend.model.entity.Rol;
import com.horarios.backend.model.entity.Usuario;
import com.horarios.backend.services.UsuarioService;

@RestController
@RequestMapping("api/googleSignIn")
public class GoogleLoginController {
	
	@Autowired
	private GoogleIdTokenVerifier tokenVerifier;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private JWTService JwtService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String tokenId) {
		Map<String, Object> response = new HashMap<String, Object>();
		Usuario usuarioExistente = null;

		try {
			GoogleIdToken idToken = this.tokenVerifier.verify(tokenId);
			if (idToken != null) {
				// Get profile information from payload
				Payload payload = idToken.getPayload();
				String email = payload.getEmail();
				String pictureUrl = (String) payload.get("picture");
				
				usuarioExistente = this.usuarioService.findByEmail(email);
				if (usuarioExistente == null) {
					// se registra el nuevo usuario y se genera su token
					usuarioExistente = new Usuario();
					usuarioExistente.setRol(new Rol());
					usuarioExistente.getRol().setId(1L);
					usuarioExistente.setEmail(email);
					usuarioExistente.setFoto(pictureUrl);
					usuarioExistente.setEnabled(true);
					usuarioExistente.setSignUpWithGoogle(true);
					this.usuarioService.save(usuarioExistente);
					
					String token = this.JwtService.createForGoogleSignIn(usuarioExistente);
					response.put("token", token);
				} else if (usuarioExistente.getSignUpWithGoogle()) {
					// se genera el token del usuario
					String token = this.JwtService.createForGoogleSignIn(usuarioExistente);
					response.put("token", token);
				} else {
					// el correo electrónico ya existe en la tabla de usuarios y no se puede proceder
					response.put("mensaje", "Ya existe un registro con el correo asociado a su cuenta de Google");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.put("mensaje", "Token ID inválido");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (IOException | GeneralSecurityException e) {
			response.put("mensaje", "Ocurrió un error interno");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return ResponseEntity.ok(response);
	}
}
