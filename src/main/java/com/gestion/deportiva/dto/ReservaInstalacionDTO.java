package com.gestion.deportiva.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservaInstalacionDTO extends BaseDTO {
	
	private static final long serialVersionUID = 572748819122752282L;

	private Long instalacionId;
	
	private String instalacionNombre;
	
	private String instalacionSedeNombre;
	
	private String instalacionSedeEmpresaNombre;
	
	private String instalacionInstalacionTipoNombre;

	private LocalDate fecha;
	
	private LocalTime hora;
	
	private LocalTime horaFin;

	private Long duracion;

}
