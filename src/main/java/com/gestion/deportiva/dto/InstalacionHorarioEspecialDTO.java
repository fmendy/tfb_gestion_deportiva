package com.gestion.deportiva.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.gestion.deportiva.validation.InstalacionHorarioEspecialValid;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@InstalacionHorarioEspecialValid
public class InstalacionHorarioEspecialDTO extends BaseDTO {

	private static final long serialVersionUID = 5026431591115314363L;

	private String empresaNombre;

	private Long empresaId;

	private String sedeNombre;

	private Long sedeId;

	private String instalacionNombre;

	@NotNull
	private Long instalacionId;

	private LocalTime horaInicio;

	private LocalTime horaFin;

	@NotNull
	private LocalDate fecha;

	private Boolean cerrado;

}
