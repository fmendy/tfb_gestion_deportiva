package com.gestion.deportiva.dto.filter;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmpresaFilter extends MaestraFilter {

	private static final long serialVersionUID = 2509056194201620867L;
	
	private List<Long> listIds;
	
	private String cif;
	

}
