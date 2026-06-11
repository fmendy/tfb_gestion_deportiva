package com.gestion.deportiva.model;

import java.io.Serializable;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;

import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class Maestra extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1623328377059858749L;

	@EqualsAndHashCode.Include
	@Column(name = "nombre", length = 255, nullable = false)
	private String nombre;

	public Maestra(Long id) {
		super(id);
	}

}
