package com.gestion.deportiva.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioTokenDTO extends BaseDTO {

	private static final long serialVersionUID = -7345416136834445361L;

	@NotNull
	private Long usuarioId;

	private String usuarioNombre;

}
