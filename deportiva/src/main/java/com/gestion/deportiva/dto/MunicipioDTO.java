package com.gestion.deportiva.dto;

import com.gestion.deportiva.validation.MunicipioCodigoIneUnicoValid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MunicipioCodigoIneUnicoValid
public class MunicipioDTO extends MaestraDTO {

	private static final long serialVersionUID = -1386726453195110761L;

	@NotNull
	private Long codigoIne;

	@NotNull
	private Long dc;

	@NotEmpty
	private String comunidadAutonomaUuid;

	private String comunidadAutonomaNombre;

	@NotEmpty
	private String provinciaUuid;

	private String provinciaNombre;
}
