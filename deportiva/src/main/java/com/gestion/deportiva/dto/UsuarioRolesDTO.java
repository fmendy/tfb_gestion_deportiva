package com.gestion.deportiva.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioRolesDTO extends BaseDTO {

	private static final long serialVersionUID = -67816429105701484L;

	private Long usuarioId;

	@NotNull
	private String usuarioUuid;

	private String usuarioNombre;

	private String usuarioEmail;

	// ✅ Nuevo campo: contendrá los UUIDs marcados en el formulario
	@NotEmpty
	private List<String> listActualRolUuid = new ArrayList<>();

	private List<UsuarioRolDTO> listUsuarioRolDTO = new ArrayList<>();
}
