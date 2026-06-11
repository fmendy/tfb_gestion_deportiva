package com.gestion.deportiva.model;

import java.io.Serializable;
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
@Table(name = "instalacion_horario")
public class InstalacionHorario extends BaseEntity implements Serializable {

	public InstalacionHorario(Long id) {
		super(id);
	}

	private static final long serialVersionUID = 4480595478530312236L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_instalacion", nullable = false)
	private Instalacion instalacion;

	@Column(name = "dia_semana", nullable = false)
	private Long diaSemana;

	@Column(name = "hora_inicio", nullable = false)
	private LocalTime horaInicio;
	
	@Column(name = "hora_fin", nullable = false)
	private LocalTime horaFin;
}
