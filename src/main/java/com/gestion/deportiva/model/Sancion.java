package com.gestion.deportiva.model;

import java.io.Serializable;
import java.time.LocalDate;

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
@Table(name = "sancion")
public class Sancion extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2296012353601977882L;

	public Sancion(Long id) {
		super(id);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", nullable = false)
	private Empresa empresa;

	@Column(name = "fecha_inicio", nullable = false)
	private LocalDate fechaInicio;

	@Column(name = "fecha_fin", nullable = false)
	private LocalDate fechaFin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sancion_tipo", nullable = false)
	private SancionTipo sancionTipo;

	@Column(name = "descripcion", length = 1250)
	private String descripcion;
}
