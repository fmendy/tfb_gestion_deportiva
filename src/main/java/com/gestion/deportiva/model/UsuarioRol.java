package com.gestion.deportiva.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_rol")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = "usuario")
public class UsuarioRol extends BaseEntity {

	private static final long serialVersionUID = -2378783107697485835L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_rol", nullable = false)
	private Rol rol;

	public UsuarioRol(Usuario usuario, Rol rol) {
		this.usuario = usuario;
		this.rol = rol;
	}

}
