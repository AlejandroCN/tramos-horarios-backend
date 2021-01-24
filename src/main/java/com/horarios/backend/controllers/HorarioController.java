package com.horarios.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.horarios.backend.model.entity.Horario;
import com.horarios.backend.services.HorarioService;

@Controller
public class HorarioController {
	
	@Autowired
	private HorarioService horarioService;
	
	@MessageMapping("/reservarHorario")	    	// a esta url se publica desde el cliente (/app/reservarHorario)
	@SendTo("/realtime/cambioHorarios")			// a esta url se subscriben los clientes  (/realtime/cambioHorarios)
	public Horario reservarHorario(Long id) {
		Horario horarioExistente = null;
		
		try {
			horarioExistente = this.horarioService.findById(id);
			if (horarioExistente == null) {
				return null;
			}
			
			horarioExistente.setContadorReservaciones(horarioExistente.getContadorReservaciones() + 1);
			this.horarioService.save(horarioExistente);
		} catch (DataAccessException ex) {
			return null;
		}
		
		return horarioExistente;
	}
	
	@MessageMapping("/liberarHorario")	    	// a esta url se publica desde el cliente (/app/reservarHorario)
	@SendTo("/realtime/cambioHorarios")			// a esta url se subscriben los clientes  (/realtime/cambioHorarios)
	public Horario liberarHorario(Long id) {
		Horario horarioExistente = null;
		
		try {
			horarioExistente = this.horarioService.findById(id);
			if (horarioExistente == null) {
				return null;
			}
			
			horarioExistente.setContadorReservaciones(horarioExistente.getContadorReservaciones() - 1);
			this.horarioService.save(horarioExistente);
		} catch (DataAccessException ex) {
			return null;
		}
		
		return horarioExistente;
	}

}
