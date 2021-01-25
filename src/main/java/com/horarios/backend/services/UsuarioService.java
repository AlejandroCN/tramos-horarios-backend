package com.horarios.backend.services;

import com.horarios.backend.model.entity.Usuario;

public interface UsuarioService {

	public Usuario save(Usuario usuario);
	
	public Usuario findByUsername(String username);
	
}
