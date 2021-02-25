package com.horarios.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

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
	 * Asocia o desasocia un horario existente a un usuario indicado.
	 * @param cambioHorario es un encapsulado que contiene el usuario autenticado y el horario que se debe asociar o desasociar.
	 * @return mensaje de error con su correspondiente bandera de error=true si no existe el usuario con el id indicado o si
	 * ocurre un error en la capa de acceso a datos. Si se realiza el cambio se devuelve tanto el usuario como el horario
	 * actualizados. 
	 */
	@MessageMapping("/seleccionarHorario")
	@SendTo("/realtime/cambioHorarios")
	public CambioHorario reservarHorario(CambioHorario cambioHorario) {
		Usuario usuarioImplicado = null;
		CambioHorario cambioRealizado = new CambioHorario();
		cambioRealizado.setError(false);
		
		try {
			usuarioImplicado = this.usuarioService.findByIdEager(cambioHorario.getUsuario().getId());
			if (usuarioImplicado == null) {
				cambioHorario.setError(true);
				cambioHorario.setMensaje("No fue posible identificar el usuario implicado en el cambio");
				return cambioHorario;
			}
			usuarioImplicado.actualizarHorarios(cambioHorario.getHorarioAModificar());
			this.usuarioService.save(usuarioImplicado);
			cambioRealizado.setHorarioAModificar(this.horarioService.findById(cambioHorario.getHorarioAModificar().getId()));
		} catch (DataAccessException ex) {
			cambioHorario.setError(true);
			cambioHorario.setMensaje("Error de acceso a datos: ".concat(ex.getMessage()).concat(": ").concat(ex.getMostSpecificCause().getMessage()));
			return cambioHorario;
		}
		
    usuarioImplicado.setPassword("");
		cambioRealizado.setUsuario(usuarioImplicado);
		return cambioRealizado;
	}

}
