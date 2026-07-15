package com.gestion.deportiva.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioPasswordDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -2477025175574652522L;

	@NotEmpty
	@Email
	@Size(max = 250)
	private String email;

}
