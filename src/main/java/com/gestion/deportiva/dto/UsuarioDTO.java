package com.gestion.deportiva.dto;

import java.util.List;

import com.gestion.deportiva.validation.UsuarioEmailUnicoValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UsuarioEmailUnicoValid
public class UsuarioDTO extends MaestraDTO {

	private static final long serialVersionUID = -1463794961252939249L;

	@Email
	@NotBlank
	@Size(max = 255)
	private String email;

	private List<UsuarioRolDTO> listUsuarioRolDTO;
}
