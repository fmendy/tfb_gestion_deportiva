package com.gestion.deportiva.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstalacionHorarioPublicoDTO {

	private LocalDate fecha; // opcional (para calculado)
	private Integer diaSemana; // 1-7 (lunes-domingo)

	private LocalTime horaInicio;
	private LocalTime horaFin;

	private boolean cerrado;
}
