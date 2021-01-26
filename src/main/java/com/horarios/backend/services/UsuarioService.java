package com.horarios.backend.services;

import com.horarios.backend.model.entity.Usuario;

public interface UsuarioService {

	public Usuario save(Usuario usuario);
	
	public Usuario findById(Long id);
	
	public Usuario findByIdEager(Long id);
	
	public Usuario findByUsername(String username);
	
	public Usuario findByEmail(String email);
	
}
