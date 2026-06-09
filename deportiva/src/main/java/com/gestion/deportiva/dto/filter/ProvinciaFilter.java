package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProvinciaFilter extends MaestraFilter {

	private static final long serialVersionUID = -5951049910368822351L;

	private String comunidadAutonomaUuid;
	
	private Long codigoIne;
}
