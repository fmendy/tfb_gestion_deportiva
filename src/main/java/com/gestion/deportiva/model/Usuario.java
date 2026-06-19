package com.gestion.deportiva.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
@FilterDef(name = "activoFilter", parameters = @ParamDef(name = "activo", type = Boolean.class))
@EqualsAndHashCode(callSuper = false, exclude = { "listUsuarioRol", "listUsuarioEmpresa", "listUsuarioSede",
		"listUsuarioInstalacion" })
@ToString(exclude = { "listUsuarioRol", "listUsuarioEmpresa", "listUsuarioSede", "listUsuarioInstalacion" })
public class Usuario extends Maestra implements Serializable {

	private static final long serialVersionUID = 3656431595003998229L;

	public Usuario(Long id) {
		super(id);
	}

	@Column(name = "email", length = 255, nullable = false)
	private String email;

	@Column(name = "password", length = 255)
	private String password;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<UsuarioRol> listUsuarioRol;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<UsuarioEmpresa> listUsuarioEmpresa;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<UsuarioSede> listUsuarioSede;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<UsuarioInstalacion> listUsuarioInstalacion;

	public List<UsuarioRol> getListUsuarioRol() {
		return listUsuarioRol.stream().filter(ud -> ud.isActivo()).toList();
	}

	public List<UsuarioEmpresa> getListUsuarioEmpresa() {
		return listUsuarioEmpresa.stream().filter(ud -> ud.isActivo()).toList();
	}

	public List<UsuarioSede> getListUsuarioSede() {
		return listUsuarioSede.stream().filter(ud -> ud.isActivo()).toList();
	}

	public List<UsuarioInstalacion> getListUsuarioInstalacion() {
		return listUsuarioInstalacion.stream().filter(ud -> ud.isActivo()).toList();
	}
}
