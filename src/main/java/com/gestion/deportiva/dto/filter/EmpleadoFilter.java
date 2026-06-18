package com.gestion.deportiva.dto.filter;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmpleadoFilter extends MaestraFilter implements Serializable {

	private static final long serialVersionUID = 5724550534213267455L;

	private String email;
	
	private List<Long> listEmpresaIds;
	
	private List<Long> listSedeIds;
	
	private List<Long> listInstalacionIds;
	
	private Long empresaId;
	
	private Long sedeId;
	
	private Long instalacionId;
	
	private Long rolId;

}
