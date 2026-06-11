package com.gestion.deportiva.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioEmpresaDTO extends BaseDTO {

	private static final long serialVersionUID = -374185460896884351L;

	private String empresaNombre;

	@NotNull
	private Long empresaId;

	@NotNull
	private Long usuarioId;

	private String usuarioNombre;

}
