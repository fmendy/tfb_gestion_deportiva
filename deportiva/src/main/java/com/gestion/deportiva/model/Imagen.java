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
@Table(name = "imagen")
public class Imagen extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -9213474286551946413L;

	public Imagen(Long id) {
		super(id);
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa",  nullable = true)
	private Empresa empresa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sede",  nullable = true)
	private Sede sede;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_instalacion",  nullable = true)
	private Instalacion instalacion;
	
	@Column(name = "url", length = 1250)
	private String url;

}
