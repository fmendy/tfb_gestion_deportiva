package com.gestion.deportiva.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SedeDTO extends MaestraDTO {

	private static final long serialVersionUID = -3379926042905925747L;

	private String municipioNombre;
	
	@NotNull
	private Long municipioId;
	
	private String empresaNombre;
	
	@NotNull
	private Long empresaId;

	@Size(max = 350)
	private String direccion;
	
	@Size(max = 255)
	private String latitud;
	
	@Size(max = 255)
	private String longitud;
	
	@NotEmpty
	@Size(max = 250)
	private String email;
	
	@Size(max = 250)
	private String logo;
	
	@Size(max = 250)
	private String url;
	
	@Size(max = 1250)
	private String descripcion;

}
