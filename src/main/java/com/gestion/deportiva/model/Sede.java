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
@Table(name = "sede")
public class Sede extends Maestra implements Serializable {

	private static final long serialVersionUID = -4091264195228309671L;

	public Sede(Long id) {
		super(id);
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio")
	private Municipio municipio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;
	
	@Column(name = "direccion", length = 350, nullable = false)
	private String direccion;
	
	@Column(name = "latitud", length = 250)
	private String latitud;
	
	@Column(name = "longitud", length = 250)
	private String longitud;
	
	@Column(name = "email", length = 250, nullable = false)
	private String email;
	
	@Column(name = "logo", length = 250)
	private String logo;
	
	@Column(name = "url", length = 250)
	private String url;
	
	@Column(name = "descripcion", length = 1250)
	private String descripcion;

}
