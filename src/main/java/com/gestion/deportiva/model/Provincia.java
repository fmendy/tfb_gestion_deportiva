package com.gestion.deportiva.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "provincia")
@EqualsAndHashCode(callSuper = false, exclude = "listMunicipio")
@ToString(exclude = "listMunicipio")
public class Provincia extends Maestra implements Serializable {

	public Provincia(Long id) {
		super(id);
	}

	private static final long serialVersionUID = 6461395023652120324L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_comunidad_autonoma")
	private ComunidadAutonoma comunidadAutonoma;

	@Column(name = "codigo_ine", nullable = false)
	private Long codigoIne;
	
	@OneToMany(mappedBy = "provincia", fetch = FetchType.LAZY)
	private List<Municipio> listMunicipio = new ArrayList<>();
}
