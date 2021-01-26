package com.horarios.backend.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	private String email;
	
	private String foto;
	
	private String password;
	
	@Column(name = "signed_up_with_google")
	private Boolean signUpWithGoogle;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rol_id")
	private Rol rol;
	
	private Boolean enabled;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="usuarios_horarios",
    joinColumns=
        @JoinColumn(name="usuario_id", referencedColumnName="id"),
    inverseJoinColumns=
        @JoinColumn(name="horario_id", referencedColumnName="id")
    )
	private List<Horario> horarios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFoto() {
		return foto;
	}
	
	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getSignUpWithGoogle() {
		return signUpWithGoogle;
	}
	
	public void setSignUpWithGoogle(Boolean signUpWithGoogle) {
		this.signUpWithGoogle = signUpWithGoogle;
	}
	
	public Rol getRol() {
		return rol;
	}
	
	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}
	
	public void actualizarHorarios(Horario horario) {
		boolean existeHorario = this.horarios.stream().anyMatch(h -> h.getId() == horario.getId());
		if (existeHorario) {
			this.horarios = this.horarios.stream().filter(h -> h.getId() != horario.getId()).collect(Collectors.toList());
		} else {
			this.horarios.add(horario);
		}
	}
	
}
