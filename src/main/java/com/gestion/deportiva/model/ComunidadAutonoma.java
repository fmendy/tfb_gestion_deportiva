package com.gestion.deportiva.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Audited;
import org.hibernate.envers.AuditTable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "comunidad_autonoma")
@EqualsAndHashCode(callSuper = false, exclude = "listProvincia")
@ToString(exclude = "listProvincia")
@Audited
@AuditTable(value = "comunidad_autonoma_historico")
public class ComunidadAutonoma extends Maestra implements Serializable {

	private static final long serialVersionUID = 3379456859352720719L;

	public ComunidadAutonoma(Long id) {
		super(id);
	}

	@Column(name = "codigo_ine", nullable = false)
	private Long codigoIne;

	@OneToMany(mappedBy = "comunidadAutonoma", fetch = FetchType.LAZY)
	private List<Provincia> listProvincia = new ArrayList<>();

}
