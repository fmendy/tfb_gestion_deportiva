package com.gestion.deportiva.service;

import java.util.List;

import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.dto.SedePublicoDTO;
import com.gestion.deportiva.dto.filter.SedeFilter;
import com.gestion.deportiva.dto.filter.SedePublicoFilter;

public interface SedeService extends MaestraService<SedeDTO, SedeFilter> {

	List<SedeDTO> getListDTOParaInstalacion(Long empresaId);
	
	List<SedeDTO> getListDTOParaEmpleado(Long empresaId);

	List<SedePublicoDTO> getListSedePublicoDTO(SedePublicoFilter filter);

	SedePublicoDTO getSedePublicoDTOById(Long id);
	
}
