package com.gestion.deportiva.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImagenDTO extends BaseDTO {

	private static final long serialVersionUID = 2548795909988123347L;

	private String empresaNombre;

	private Long empresaId;
	
	private String sedeNombre;

	private Long sedeId;

	private String instalacionNombre;

	private Long instalacionId;

	@Size(max = 1250)
	private String url;

}
