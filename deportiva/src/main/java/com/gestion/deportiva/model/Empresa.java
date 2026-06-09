package com.gestion.deportiva.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "empresa")
public class Empresa extends Maestra implements Serializable {

	private static final long serialVersionUID = 2705137788402525702L;

	public Empresa(Long id) {
		super(id);
	}
	
	@Column(name = "email", length = 250, nullable = false)
	private String email;
	
	@Column(name = "logo", length = 250)
	private String logo;
	
	@Column(name = "url", length = 250)
	private String url;
	
	@Column(name = "descripcion", length = 1250)
	private String descripcion;

}
