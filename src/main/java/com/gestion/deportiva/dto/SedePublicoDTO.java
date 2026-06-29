package com.gestion.deportiva.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SedePublicoDTO extends MaestraDTO {

	private static final long serialVersionUID = 4100741280888604015L;

	private String comunidadAutonomaNombre;

	private Long comunidadAutonomaId;

	private Long provinciaId;

	private String provinciaNombre;

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
	private String url;

	@Size(max = 1250)
	private String descripcion;

	private String empresaSedeNombre;
	
	private String logo;

	private List<InstalacionDTO> listInstalacion;
}
