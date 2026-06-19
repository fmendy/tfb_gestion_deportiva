package com.gestion.deportiva.dto.filter;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstalacionFilter extends MaestraFilter {
	
	private static final long serialVersionUID = 8092188956861905703L;
	
	private Long empresaId;
	
	private Long sedeId;
	
	private Long instalacionTipoId;
	
	private List<Long> listEmpresaIds;
	
	private List<Long> listSedeIds;
	
	private List<Long> listInstalacionIds;

}
