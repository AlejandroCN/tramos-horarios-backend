package com.horarios.backend.services;

import java.util.List;

import com.horarios.backend.model.entity.Horario;

public interface HorarioService {
	
	public List<Horario> findAll();
	
	public Horario save(Horario horario);
	
	public Horario findById(Long id);

}
