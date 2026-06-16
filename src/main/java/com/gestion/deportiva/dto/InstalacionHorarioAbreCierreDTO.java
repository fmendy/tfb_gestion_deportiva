package com.gestion.deportiva.dto;

import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionHorarioAbreCierreDTO extends BaseDTO {

	private static final long serialVersionUID = -2630955013997587675L;

	private LocalTime horaInicio;

	private LocalTime horaFin;
}
