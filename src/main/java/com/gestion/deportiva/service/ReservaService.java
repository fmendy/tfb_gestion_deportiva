package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaInstalacionDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;

public interface ReservaService extends BaseService<ReservaDTO, ReservaFilter> {
	
	ReservaInstalacionDTO getReservaInstalacionDTOByInstalacionIdAndReservaInstalacionDTO(Long instalacionId, ReservaInstalacionDTO dto);

}
