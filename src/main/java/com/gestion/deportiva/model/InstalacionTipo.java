package com.gestion.deportiva.model;

import java.io.Serializable;

import org.hibernate.annotations.Audited;
import org.hibernate.envers.AuditTable;

import jakarta.persistence.Column;
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
@Table(name = "instalacion_tipo")
@Audited
@AuditTable(value = "instalacion_tipo_historico")
@EqualsAndHashCode(callSuper = false)
public class InstalacionTipo extends Maestra implements Serializable {

	private static final long serialVersionUID = 3379456859352720719L;

	public InstalacionTipo(Long id) {
		super(id);
	}
	
	@Column(name = "descripcion", length = 1250)
	private String descripcion;



}
