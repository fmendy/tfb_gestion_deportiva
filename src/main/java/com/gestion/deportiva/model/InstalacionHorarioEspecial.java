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
@Table(name = "instalacion_horario_especial")
public class InstalacionHorarioEspecial extends BaseEntity implements Serializable {


	private static final long serialVersionUID = 3923625792565376583L;

	public InstalacionHorarioEspecial(Long id) {
		super(id);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_instalacion", nullable = false)
	private Instalacion instalacion;

	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;

	@Column(name = "hora_inicio")
	private LocalTime horaInicio;
	
	@Column(name = "hora_fin")
	private LocalTime horaFin;
	
	@Column(name = "cerrado" ,nullable = false)
	private Boolean cerrado;
}
