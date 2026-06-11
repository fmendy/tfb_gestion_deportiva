package com.gestion.deportiva.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioInstalacionDTO extends BaseDTO {

	private static final long serialVersionUID = 5439062557499175174L;

	private String empresaNombre;

	private Long empresaId;

	private String sedeNombre;

	private Long sedeId;

	private String instalacionNombre;

	@NotNull
	private Long instalacionId;

	@NotNull
	private Long usuarioId;

	private String usuarioNombre;

}
