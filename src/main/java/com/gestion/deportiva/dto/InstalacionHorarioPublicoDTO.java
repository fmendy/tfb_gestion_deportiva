package com.gestion.deportiva.dto;

import java.io.Serializable;
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
public class InstalacionHorarioPublicoDTO implements Serializable {

	private static final long serialVersionUID = 2417494153930195657L;
	
	private LocalDate fecha; 
	private Integer diaSemana; // 1-7 (lunes-domingo)

	private LocalTime horaInicio;
	private LocalTime horaFin;

	private boolean cerrado;
}
