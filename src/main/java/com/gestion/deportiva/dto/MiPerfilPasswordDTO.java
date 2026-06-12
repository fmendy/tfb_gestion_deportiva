package com.gestion.deportiva.dto;



import com.gestion.deportiva.validation.MiPerfilPasswordValid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MiPerfilPasswordValid
public class MiPerfilPasswordDTO extends BaseDTO {

	private static final long serialVersionUID = 4324482952546323280L;

	@NotEmpty
	private String passwordActual;
	
	@NotEmpty
	@Size(min = 8)
	private String password;
	
	@NotEmpty
	@Size(min = 8)
	private String passwordConfirmar;

}
