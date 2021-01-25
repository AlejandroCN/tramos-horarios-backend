package com.horarios.backend.auth.service;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horarios.backend.auth.SimpleGrantedAuthorityMixin;
import com.horarios.backend.model.entity.Usuario;
import com.horarios.backend.services.UsuarioService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTServiceImpl implements JWTService {
	
	public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public static final long EXPIRATION_DATE = 3 * 60 * 60 * 1000;
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String HEADER_STRING = "Authorization";
	
	Logger logger = LoggerFactory.getLogger(JWTServiceImpl.class);
	
	@Autowired
	private UsuarioService usuarioService;

	@Transactional(readOnly = true)
	@Override
	public String create(Authentication auth) throws IOException {
		String username = ((User) auth.getPrincipal()).getUsername();
		Usuario usuarioExistente = this.usuarioService.findByUsername(username);		
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		claims.put("usuario", new ObjectMapper().writeValueAsString(usuarioExistente));
		claims.put("expiration", new ObjectMapper().writeValueAsString(EXPIRATION_DATE));

		String token = Jwts.builder().setClaims(claims).setSubject(username)
				.signWith(SECRET_KEY).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE)).compact();
		return token;
	}

	@Override
	public boolean validate(String token) {
		try {
			getClaims(token);

			return true;
		} catch (JwtException | IllegalArgumentException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public Claims getClaims(String token) {
		Claims claims = Jwts
			.parserBuilder().setSigningKey(SECRET_KEY)
			.build()
			.parseClaimsJws(resolve(token)).getBody();
		return claims;
	}

	@Override
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get("authorities");

		Collection<? extends GrantedAuthority> authorities = Arrays
				.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
						.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

		return authorities;
	}

	@Override
	public String resolve(String token) {
		if (token != null && token.startsWith(TOKEN_PREFIX)) {
			return token.replace(TOKEN_PREFIX, "");
		}
		return null;
	}

}
