package com.horarios.backend.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.horarios.backend.model.entity.Usuario;

public interface UsuarioDao extends CrudRepository<Usuario, Long> {
	
	public Usuario findByUsername(String username);
	
	public Usuario findByEmail(String email);

}
