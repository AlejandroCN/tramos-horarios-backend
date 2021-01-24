package com.horarios.backend;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

@Configuration
public class MvcConfig {
	
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String googleClientId;
	
	@Bean
	public NetHttpTransport netHttpTransport() {
		return new NetHttpTransport();
	}
	
	@Bean
	public JsonFactory jsonFactory() {
		return new GsonFactory();
	}
	
	@Bean
	public GoogleIdTokenVerifier googleIdTokenVerifier() {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport(), jsonFactory())
				  .setAudience(Collections.singletonList(googleClientId))
				  .build();
		return verifier;
	}

}
