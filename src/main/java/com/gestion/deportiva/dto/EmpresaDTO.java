package com.gestion.deportiva.dto;

import org.springframework.web.multipart.MultipartFile;

import com.gestion.deportiva.validation.CifValid;
import com.gestion.deportiva.validation.EmpresaCifUnicoValid;
import com.gestion.deportiva.validation.EmpresaEmailUnicoValid;
import com.gestion.deportiva.validation.EmpresaNombreUnicoValid;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EmpresaEmailUnicoValid
@EmpresaNombreUnicoValid
@EmpresaCifUnicoValid
public class EmpresaDTO extends MaestraDTO {

	private static final long serialVersionUID = -4826841690566784966L;
	
	@NotEmpty
	@Size(max = 250)
	private String email;
	
	private MultipartFile logo;
	
	private String logoUrl;
	
	@Size(max = 250)
	private String url;
	
	@Size(max = 1250)
	private String descripcion;
	
	@Size(max = 20)
	@NotEmpty
	@CifValid(message = "{error.validacion.cif.invalido}")
	private String cif;

}
