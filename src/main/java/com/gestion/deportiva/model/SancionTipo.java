package com.gestion.deportiva.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sancion_tipo")
@EqualsAndHashCode(callSuper = false)
public class SancionTipo extends Maestra implements Serializable {

	private static final long serialVersionUID = 1577838903074952830L;

	public SancionTipo(Long id) {
		super(id);
	}

}
