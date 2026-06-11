package com.gestion.deportiva.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioRolDTO extends BaseDTO {

	private static final long serialVersionUID = 6559196070684588855L;

	private Long usuarioId;

	@NotNull
	private String usuarioUuid;

	private String usuarioNombre;

	private String usuarioEmail;

	private Long rolId;

	@NotNull
	private String rolUuid;

	private String rolNombre;
}
