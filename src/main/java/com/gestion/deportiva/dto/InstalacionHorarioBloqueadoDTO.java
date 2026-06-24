package com.gestion.deportiva.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.gestion.deportiva.validation.InstalacionHorarioBloqueadoValid;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@InstalacionHorarioBloqueadoValid
public class InstalacionHorarioBloqueadoDTO extends BaseDTO {

	private static final long serialVersionUID = 2436543314528281960L;

	private String empresaNombre;

	private Long empresaId;

	private String sedeNombre;

	private Long sedeId;

	private String instalacionNombre;

	@NotNull
	private Long instalacionId;

	@NotNull
	private LocalTime horaInicio;

	@NotNull
	private LocalTime horaFin;

	@NotNull
	private LocalDate fecha;

}
