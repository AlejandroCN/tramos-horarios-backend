package com.horarios.backend.model.pojo;

public class StompMessage {
	
	private String mensaje;
	
	private Boolean error;
	
	public StompMessage() {
	}

	public StompMessage(String mensaje, Boolean error) {
		super();
		this.mensaje = mensaje;
		this.error = error;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}
	
}
