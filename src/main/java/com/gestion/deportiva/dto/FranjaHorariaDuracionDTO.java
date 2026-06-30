package com.gestion.deportiva.dto;

import java.io.Serializable;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FranjaHorariaDuracionDTO implements Serializable {

	private static final long serialVersionUID = 2332240666532029098L;

	private Long minutos;

	private LocalTime horaFin;
}
