package com.gestion.deportiva.dto;



import com.gestion.deportiva.validation.UsuarioCambioPasswordValid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UsuarioCambioPasswordValid
public class MiPerfilPasswordDTO extends BaseDTO {

	private static final long serialVersionUID = 4324482952546323280L;

	@NotEmpty
	private String passwordActual;
	
	@NotEmpty
	@Size(min = 8)
	private String passwordNuevo;
	
	@NotEmpty
	@Size(min = 8)
	private String passwordNuevoRepetido;

}
