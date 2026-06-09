package com.gestion.deportiva.dto;

import com.gestion.deportiva.validation.ProvinciaCodigoIneUnicoValid;
import com.gestion.deportiva.validation.ProvinciaNombreUnicoValid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ProvinciaCodigoIneUnicoValid
@ProvinciaNombreUnicoValid
public class ProvinciaDTO extends MaestraDTO {

	private static final long serialVersionUID = -1386726453195110761L;

	@NotNull
	private Long codigoIne;

	@NotEmpty
	private String comunidadAutonomaUuid;

	private String comunidadAutonomaNombre;
}
