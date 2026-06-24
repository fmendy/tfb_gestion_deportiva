package com.gestion.deportiva.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name = "instalacion_horario_bloqueado")
public class InstalacionHorarioBloqueado extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6794791456563667181L;

	public InstalacionHorarioBloqueado(Long id) {
		super(id);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_instalacion", nullable = false)
	private Instalacion instalacion;

	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;

	@Column(name = "hora_inicio", nullable = false)
	private LocalTime horaInicio;

	@Column(name = "hora_fin", nullable = false)
	private LocalTime horaFin;
}
