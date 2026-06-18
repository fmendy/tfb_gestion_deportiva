package com.gestion.deportiva.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionDTO extends MaestraDTO {

	private static final long serialVersionUID = 2548795909988123347L;

	private String empresaNombre;

	private Long empresaId;

	private String sedeNombre;

	@NotNull
	private Long sedeId;

	private String instalacionTipoNombre;

	@NotNull
	private Long instalacionTipoId;

	@Size(max = 1250)
	private String descripcion;
	
	private String empresaSedeInstalacionNombre;

}
