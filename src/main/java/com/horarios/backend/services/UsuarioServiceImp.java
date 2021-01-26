package com.horarios.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.horarios.backend.model.dao.UsuarioDao;
import com.horarios.backend.model.entity.Usuario;

@Service
public class UsuarioServiceImp implements UsuarioService {

	@Autowired
	private UsuarioDao usuarioDao;
	
	@Transactional
	@Override
	public Usuario save(Usuario usuario) {
		return this.usuarioDao.save(usuario);
	}

	@Transactional(readOnly = true)
	@Override
	public Usuario findByUsername(String username) {
		return this.usuarioDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	@Override
	public Usuario findByEmail(String email) {
		return this.usuarioDao.findByEmail(email);
	}

}
