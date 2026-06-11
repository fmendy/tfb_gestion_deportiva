package com.gestion.deportiva.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioSedeDTO extends BaseDTO {

	private static final long serialVersionUID = -6042894638436727134L;

	private String empresaNombre;

	private Long empresaId;

	private String sedeNombre;

	@NotNull
	private Long sedeId;


	@NotNull
	private Long usuarioId;

	private String usuarioNombre;

}
