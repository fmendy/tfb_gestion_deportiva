package com.gestion.deportiva.model;

import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "permiso")
public class Permiso extends Maestra {

	private static final long serialVersionUID = -6687887173675179106L;

	public Permiso(Long id) {
		super(id);
	}

}
