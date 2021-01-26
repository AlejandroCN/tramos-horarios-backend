package com.horarios.backend.model.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.horarios.backend.model.entity.Usuario;

public interface UsuarioDao extends CrudRepository<Usuario, Long> {
	
	public Usuario findByUsername(String username);
	
	public Usuario findByEmail(String email);
	
	@Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.rol LEFT JOIN FETCH u.horarios WHERE u.id=?1")
	public Usuario findByIdEager(Long id);

}
