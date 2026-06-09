package com.gestion.deportiva.dto;

import java.io.Serializable;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioRegistroDTO extends MaestraDTO implements Serializable {

	private static final long serialVersionUID = -2477025175574652522L;

	@Email
	private String email;

	@NotEmpty
	private String password;

	@NotEmpty
	private String password2;



}
