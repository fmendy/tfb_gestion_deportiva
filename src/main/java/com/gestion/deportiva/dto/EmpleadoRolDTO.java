package com.gestion.deportiva.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmpleadoRolDTO extends BaseDTO {

	private static final long serialVersionUID = 6559196070684588855L;

	@NotNull
	private Long usuarioId;

	@NotNull
	private Long rolId;

	private String rolNombre;

	private List<Long> listEmpresaId;

	private List<Long> listSedeId;

	private List<Long> listInstalacionId;
}
