package com.gestion.deportiva.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "instalacion")
public class Instalacion extends Maestra implements Serializable {

	private static final long serialVersionUID = -9213474286551946413L;

	public Instalacion(Long id) {
		super(id);
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sede")
	private Sede sede;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_instalacion_tipo")
	private InstalacionTipo instalacionTipo;
	
	@Column(name = "descripcion", length = 1250)
	private String descripcion;

}
