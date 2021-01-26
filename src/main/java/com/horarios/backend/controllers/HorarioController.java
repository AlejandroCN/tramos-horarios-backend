package com.horarios.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.horarios.backend.model.entity.Horario;
import com.horarios.backend.model.entity.Usuario;
import com.horarios.backend.model.pojo.CambioHorario;
import com.horarios.backend.services.HorarioService;
import com.horarios.backend.services.UsuarioService;

@Controller
public class HorarioController {
	
	@Autowired
	private HorarioService horarioService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	Logger logger = LoggerFactory.getLogger(HorarioController.class);
	
	@MessageMapping("/reservarHorario")	    	// a esta url se publica desde el cliente (/app/reservarHorario)
	@SendTo("/realtime/cambioHorarios")			// a esta url se subscriben los clientes  (/realtime/cambioHorarios)
	public CambioHorario reservarHorario(CambioHorario cambioHorario) {
		Horario horarioExistente = null;
		Usuario usuarioImplicado = null;
		CambioHorario cambioRealizado = new CambioHorario();
		
		try {
			usuarioImplicado = this.usuarioService.findByIdEager(cambioHorario.getUsuario().getId());
			horarioExistente = this.horarioService.findById(cambioHorario.getHorarioAModificar().getId());
			if (horarioExistente == null || usuarioImplicado == null) {
				return null;
			}
			
			horarioExistente.setContadorReservaciones(horarioExistente.getContadorReservaciones() + 1);
			cambioRealizado.setHorarioAModificar(this.horarioService.save(horarioExistente));
			
			usuarioImplicado.actualizarHorarios(cambioRealizado.getHorarioAModificar());
			this.usuarioService.save(usuarioImplicado);
			cambioRealizado.setUsuario(usuarioImplicado);
		} catch (DataAccessException ex) {
			return null;
		}
		
		return cambioRealizado;
	}
	
	@MessageMapping("/liberarHorario")	    	// a esta url se publica desde el cliente (/app/reservarHorario)
	@SendTo("/realtime/cambioHorarios")			// a esta url se subscriben los clientes  (/realtime/cambioHorarios)
	public CambioHorario liberarHorario(CambioHorario cambioHorario) {
		Horario horarioExistente = null;
		Usuario usuarioImplicado = null;
		CambioHorario cambioRealizado = new CambioHorario();
		
		try {
			usuarioImplicado = this.usuarioService.findByIdEager(cambioHorario.getUsuario().getId());
			horarioExistente = this.horarioService.findById(cambioHorario.getHorarioAModificar().getId());
			if (horarioExistente == null || usuarioImplicado == null) {
				return null;
			}
			
			horarioExistente.setContadorReservaciones(horarioExistente.getContadorReservaciones() - 1);
			cambioRealizado.setHorarioAModificar(this.horarioService.save(horarioExistente));
			
			usuarioImplicado.actualizarHorarios(cambioRealizado.getHorarioAModificar());
			this.usuarioService.save(usuarioImplicado);
			cambioRealizado.setUsuario(usuarioImplicado);
		} catch (DataAccessException ex) {
			return null;
		}
		
		return cambioRealizado;
	}

}
