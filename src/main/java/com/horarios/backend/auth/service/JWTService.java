package com.horarios.backend.auth.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.horarios.backend.model.entity.Usuario;

import io.jsonwebtoken.Claims;

public interface JWTService {
	
	public String create(Authentication auth) throws IOException;
	
	public String createForGoogleSignIn(Usuario usuario) throws IOException;
	
	public boolean validate(String token);
	
	public Claims getClaims(String token);
	
	public String getUsername(String token);
	
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
	
	public String resolve(String token);

}
