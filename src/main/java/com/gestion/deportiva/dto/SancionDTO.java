package com.gestion.deportiva.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SancionDTO extends BaseDTO {

	private static final long serialVersionUID = 8964260052246620996L;

	private String empresaNombre;

	@NotNull
	private Long empresaId;

	private String usuarioNombre;

	@NotNull
	private Long usuarioId;

	private String sancionTipoNombre;

	@NotNull
	private Long sancionTipoId;

	@NotNull
	private LocalDate fechaInicio;

	@NotNull
	private LocalDate fechaFin;
	
	@Size(max = 1250)
	private String descripcion;

}
