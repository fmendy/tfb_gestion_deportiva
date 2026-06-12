package com.gestion.deportiva.dto;

import java.util.Date;

import com.gestion.deportiva.validation.FieldMatchValid;

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
public class MiPerfilDTO extends MaestraDTO {

	private static final long serialVersionUID = 5443997554621493300L;
	
	
	@NotEmpty
	@Email
	@Size(max = 250)
	private String email;

	@NotEmpty
	private String emailConfirmar;

	private Date fechaAlta;

}
