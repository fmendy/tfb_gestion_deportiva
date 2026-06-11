package com.gestion.deportiva.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionConfiguracionReservaDTO extends BaseDTO {

	private static final long serialVersionUID = 8828334797129917468L;

	private String empresaNombre;

	private Long empresaId;
	
	private String sedeNombre;

	private Long sedeId;

	private String instalacionNombre;

	@NotNull
	private Long instalacionId;

	@NotNull
	private Long duracionMin;
	
	@NotNull
	private Long duracionMax;
	
	@NotNull
	private Long intervaloHorario;

}
