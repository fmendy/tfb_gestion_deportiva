package com.gestion.deportiva.dto;

import com.gestion.deportiva.validation.CifValid;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmpresaDTO extends MaestraDTO {

	private static final long serialVersionUID = -4826841690566784966L;
	
	@NotEmpty
	@Size(max = 250)
	private String email;
	
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
