package com.horarios.backend.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.horarios.backend.model.entity.Horario;

public interface HorarioDao extends CrudRepository<Horario, Long> {

}
