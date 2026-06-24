package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioBloqueadoFilter;

public interface InstalacionHorarioBloqueadoService extends BaseService<InstalacionHorarioBloqueadoDTO, InstalacionHorarioBloqueadoFilter> {

	InstalacionHorarioBloqueadoDTO findByIdOrNewEmpty(Long id, Long instalacionId);
	


}
