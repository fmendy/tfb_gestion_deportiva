package com.gestion.deportiva.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionHorarioDTO extends BaseDTO {

	private static final long serialVersionUID = 5026431591115314363L;

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
	private Long diaSemana;

}
