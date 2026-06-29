package com.gestion.deportiva.service;

import java.util.List;

import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.dto.SedeMapaDTO;
import com.gestion.deportiva.dto.filter.SedeFilter;
import com.gestion.deportiva.dto.filter.SedeMapaFilter;

public interface SedeService extends MaestraService<SedeDTO, SedeFilter> {

	List<SedeDTO> getListDTOParaInstalacion(Long empresaId);
	
	List<SedeDTO> getListDTOParaEmpleado(Long empresaId);

	List<SedeMapaDTO> getListSedeMapaDTO(SedeMapaFilter filter);
	
}
