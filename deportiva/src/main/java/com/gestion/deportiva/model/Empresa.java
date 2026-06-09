package com.gestion.deportiva.model;

import java.io.Serializable;


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

}
