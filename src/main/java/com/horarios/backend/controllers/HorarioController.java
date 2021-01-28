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
	
	/**
	 * Asocia un horario existente a un usuario indicado, además, incrementa en 1 unidad el valor del atributo
	 * contadorReservaciones de dicho horario.
	 * Este método deberia ser llamado desde el cliente solo si el usuario no tiene actualmente asociado el horario a modificar,
	 * contiene una validación que previene esa situación ya que causaría inconsistencias en el contador de reservaciones.
	 * @param cambioHorario es un encapsulado que contiene el usuario autenticado y el horario que se debe asociar y actualizar.
	 * @return null si no existe el horario con el id indicado, si no existe el usuario con el id indicado o si ocurre un
	 * error en la capa de acceso a datos. Si se realiza el cambio se devuelve tanto el usuario como el horario actualizados,
	 * si no ocurre el cambio se devuelve el mismo objeto de tipo CambioHorario que se recibe por parámetro. 
	 */
	@MessageMapping("/reservarHorario")
	@SendTo("/realtime/cambioHorarios")
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
			
			if (!usuarioImplicado.getHorarios().stream().anyMatch(h -> h.getId() == cambioHorario.getHorarioAModificar().getId())) {
				horarioExistente.setContadorReservaciones(horarioExistente.getContadorReservaciones() + 1);
				cambioRealizado.setHorarioAModificar(this.horarioService.save(horarioExistente));
				
				usuarioImplicado.actualizarHorarios(cambioRealizado.getHorarioAModificar());
				this.usuarioService.save(usuarioImplicado);
				cambioRealizado.setUsuario(usuarioImplicado);
			} else {
				return cambioHorario;
			}
		} catch (DataAccessException ex) {
			return null;
		}
		
		return cambioRealizado;
	}
	
	/**
	 * Desasocia un horario existente de un usuario indicado, además, decrementa en 1 unidad el valor del atributo
	 * contadorReservaciones de dicho horario.
	 * Este método deberia ser llamado desde el cliente solo si el usuario tiene actualmente asociado el horario a modificar,
	 * contiene una validación que previene esa situación ya que causaría inconsistencias en el contador de reservaciones.
	 * @param cambioHorario es un encapsulado que contiene el usuario autenticado y el horario que se debe asociar y actualizar.
	 * @return null si no existe el horario con el id indicado, si no existe el usuario con el id indicado o si ocurre un
	 * error en la capa de acceso a datos. Si se realiza el cambio se devuelve tanto el usuario como el horario actualizados,
	 * si no ocurre el cambio se devuelve el mismo objeto de tipo CambioHorario que se recibe por parámetro. 
	 */
	@MessageMapping("/liberarHorario")
	@SendTo("/realtime/cambioHorarios")
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
			
			if (usuarioImplicado.getHorarios().stream().anyMatch(h -> h.getId() == cambioHorario.getHorarioAModificar().getId())) {
				horarioExistente.setContadorReservaciones(horarioExistente.getContadorReservaciones() - 1);
				cambioRealizado.setHorarioAModificar(this.horarioService.save(horarioExistente));
				
				usuarioImplicado.actualizarHorarios(cambioRealizado.getHorarioAModificar());
				this.usuarioService.save(usuarioImplicado);
				cambioRealizado.setUsuario(usuarioImplicado);
			} else {
				return cambioHorario;
			}
		} catch (DataAccessException ex) {
			return null;
		}
		
		return cambioRealizado;
	}

}
