package com.gestion.deportiva.dto;

import com.gestion.deportiva.validation.CifValid;
import com.gestion.deportiva.validation.FieldMatchValid;
import com.gestion.deportiva.validation.EmpresaRegistroEmailUnicoValid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EmpresaRegistroEmailUnicoValid
@FieldMatchValid(first = "email", second = "emailConfirmar", message = "{error.validacion.registro.empresa.email.no.coincide}")
@FieldMatchValid(first = "password", second = "passwordConfirmar", message = "{error.validacion.registro.empresa.password.no.coincide}")
public class EmpresaRegistroDTO extends MaestraDTO {

	private static final long serialVersionUID = 2505824822273595331L;

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

	@Size(max = 250)
	private String logo;

	@Size(max = 250)
	private String url;

	@Size(max = 1250)
	private String descripcion;
	
	@Size(max = 20)
	@NotEmpty
	@CifValid(message = "{error.validacion.cif.invalido}")
	private String cif;
}
