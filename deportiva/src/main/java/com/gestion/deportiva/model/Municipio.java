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
@Table(name = "municipio")
public class Municipio extends Maestra implements Serializable {

	public Municipio(Long id) {
		super(id);
	}

	private static final long serialVersionUID = 4480595478530312236L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_provincia")
	private Provincia provincia;

	@Column(name = "codigo_ine", nullable = false)
	private Long codigoIne;

	@Column(name = "dc", nullable = false)
	private Long dc;
}
