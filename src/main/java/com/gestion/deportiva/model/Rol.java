package com.gestion.deportiva.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rol")
public class Rol extends Maestra {

	private static final long serialVersionUID = 3656431595003998229L;

	public Rol(Long id) {
		super(id);
	}
	
	@OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
	private List<RolPermiso> listRolPermiso = new ArrayList<>();

}
