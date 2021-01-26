package com.horarios.backend.model.pojo;

import java.io.Serializable;

import com.horarios.backend.model.entity.Horario;
import com.horarios.backend.model.entity.Usuario;

/**
 * Clase simple que contiene un usuario y un horario a modificar
 * @author AlejandroCN
 */
public class CambioHorario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	
	private Horario horarioAModificar;
	
	public CambioHorario() {
	}

	public CambioHorario(Usuario usuario, Horario horarioAModificar) {
		super();
		this.usuario = usuario;
		this.horarioAModificar = horarioAModificar;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Horario getHorarioAModificar() {
		return horarioAModificar;
	}

	public void setHorarioAModificar(Horario horarioAModificar) {
		this.horarioAModificar = horarioAModificar;
	}
	
}
