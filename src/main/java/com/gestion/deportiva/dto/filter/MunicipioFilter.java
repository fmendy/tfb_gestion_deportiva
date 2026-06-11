package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MunicipioFilter extends MaestraFilter {

	private static final long serialVersionUID = 7664502603084041325L;

	private String comunidadAutonomaUuid;
	
	private String provinciaUuid;
	
	private Long codigoIne;
	
	private Long dc;
}
