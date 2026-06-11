package com.gestion.deportiva.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionTipoDTO extends MaestraDTO {

	private static final long serialVersionUID = -1386726453195110761L;

	@Size(max = 1250)
	private String descripcion;

}
