package com.horarios.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.horarios.backend.model.dao.HorarioDao;
import com.horarios.backend.model.entity.Horario;

@Service
public class HorarioServiceImp implements HorarioService {
	
	@Autowired
	private HorarioDao horarioDao;

	@Override
	@Transactional(readOnly = true)
	public List<Horario> findAll() {
		return (List<Horario>) this.horarioDao.findAll();
	}

	@Override
	@Transactional
	public Horario save(Horario horario) {
		return this.horarioDao.save(horario);
	}

	@Override
	@Transactional(readOnly = true)
	public Horario findById(Long id) {
		return this.horarioDao.findById(id).orElse(null);
	}

}
