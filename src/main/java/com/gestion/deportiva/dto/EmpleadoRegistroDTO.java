package com.gestion.deportiva.dto;

import com.gestion.deportiva.validation.EmpleadoRegistroEmailUnicoValid;
import com.gestion.deportiva.validation.FieldMatchValid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EmpleadoRegistroEmailUnicoValid
@FieldMatchValid(first = "email", second = "emailConfirmar", message = "{error.validacion.registro.empleado.email.no.coincide}")
@FieldMatchValid(first = "password", second = "passwordConfirmar", message = "{error.validacion.registro.empleado.password.no.coincide}")

public class EmpleadoRegistroDTO extends MaestraDTO {

	private static final long serialVersionUID = 6085452575585588975L;
	@Email
	@NotBlank
	@Size(max = 255)
	private String email;

	@NotEmpty
	private String emailConfirmar;

	@NotEmpty
	@Size(min = 8, max = 250)
	private String password;

	@NotEmpty
	private String passwordConfirmar;
	
	@NotNull
	private Long rolId;
	
	private Long empresaId;
	
	private Long sedeId;
	
	private Long instalacionId;
	

}
