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
@Table(name = "reserva_estado")
@EqualsAndHashCode(callSuper = false)
public class ReservaEstado extends Maestra implements Serializable {

	private static final long serialVersionUID = -3775414962580004211L;

	public ReservaEstado(Long id) {
		super(id);
	}


}
