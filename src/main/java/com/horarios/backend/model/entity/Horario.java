package com.horarios.backend.model.entity;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "horarios")
public class Horario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Time hora;
	
	@Column(name = "contador_reservaciones")
	private int contadorReservaciones;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Time getHora() {
		return hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public int getContadorReservaciones() {
		return contadorReservaciones;
	}

	public void setContadorReservaciones(int contadorReservaciones) {
		this.contadorReservaciones = contadorReservaciones;
	}
	
}
