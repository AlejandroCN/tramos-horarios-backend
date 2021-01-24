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

@RestController
@RequestMapping("api/googleSignIn")
public class GoogleLoginController {
	
	@Autowired
	private GoogleIdTokenVerifier tokenVerifier;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String tokenId) {
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			GoogleIdToken idToken = this.tokenVerifier.verify(tokenId);
			if (idToken != null) {
				Payload payload = idToken.getPayload();
			
				// Get profile information from payload
				String email = payload.getEmail();
				String name = (String) payload.get("name");
				String pictureUrl = (String) payload.get("picture");
				
				response.put("email", email);
				response.put("name", name);
				response.put("pictureUrl", pictureUrl);
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
