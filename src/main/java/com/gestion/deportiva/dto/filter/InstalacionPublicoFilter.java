package com.gestion.deportiva.dto.filter;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionPublicoFilter extends MaestraFilter {

	private static final long serialVersionUID = -1635142981256591152L;

	@NotNull
	private Long instalacionTipoId;

	@NotNull
	private LocalDate fecha;

	@NotNull
	private LocalTime horaInicio;

	@NotNull
	private Long municipioId;

}
