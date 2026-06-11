package com.gestion.deportiva.dto;

import com.gestion.deportiva.validation.ComunidadAutonomaCodigoIneUnicoValid;
import com.gestion.deportiva.validation.ComunidadAutonomaNombreUnicoValid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ComunidadAutonomaCodigoIneUnicoValid
@ComunidadAutonomaNombreUnicoValid
public class ComunidadAutonomaDTO extends MaestraDTO {

	private static final long serialVersionUID = -1386726453195110761L;

	@NotNull
	private Long codigoIne;

}
