package com.gestion.deportiva.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SedeFilter extends MaestraFilter {
	
	private static final long serialVersionUID = 8092188956861905703L;
	
	private String demarcacionUuid;

}
