package com.gestion.deportiva.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmpleadoDTO extends MaestraDTO {

	private static final long serialVersionUID = 6085452575585588975L;

	@Email
	@NotBlank
	@Size(max = 255)
	private String email;

	private List<UsuarioRolDTO> listUsuarioRolDTO;

	private List<UsuarioEmpresaDTO> listUsuarioEmpresaDTO;

	private List<UsuarioSedeDTO> listUsuarioSedeDTO;

	private List<UsuarioInstalacionDTO> listUsuarioInstalacionDTO;

}
