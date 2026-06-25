package com.gestion.deportiva.dto;

import java.io.Serializable;

import com.gestion.deportiva.validation.FieldMatchValid;
import com.gestion.deportiva.validation.UsuarioEmailUnicoValid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@FieldMatchValid(first = "email", second = "emailConfirmar", message = "{error.validacion.registro.usuario.email.no.coincide}")
@FieldMatchValid(first = "password", second = "passwordConfirmar", message = "{error.validacion.registro.usuario.password.no.coincide}")
@UsuarioEmailUnicoValid
public class UsuarioRegistroDTO extends MaestraDTO implements Serializable {

	private static final long serialVersionUID = -2477025175574652522L;

	@NotEmpty
	@Email
	@Size(max = 250)
	private String email;

	@NotEmpty
	private String emailConfirmar;

	@NotEmpty
	@Size(min = 8, max = 250)
	private String password;

	@NotEmpty
	private String passwordConfirmar;



}
