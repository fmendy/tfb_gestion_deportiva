package com.gestion.deportiva.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rol_permiso")
@Getter
@Setter
@NoArgsConstructor
public class RolPermiso extends BaseEntity {

	private static final long serialVersionUID = -2378783107697485835L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_permiso", nullable = false)
	private Permiso permiso;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_rol", nullable = false)
	private Rol rol;

	public RolPermiso(Permiso permiso, Rol rol) {
		this.permiso = permiso;
		this.rol = rol;
	}

}
