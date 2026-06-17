package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.InstalacionConfiguracionReservaDTO;
import com.gestion.deportiva.dto.filter.InstalacionConfiguracionReservaFilter;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;

public interface InstalacionConfiguracionReservaService extends BaseService<InstalacionConfiguracionReservaDTO, InstalacionConfiguracionReservaFilter> {

	InstalacionConfiguracionReservaDTO findDTOByInstalacionId(Long instalacionId);

	InstalacionConfiguracionReserva findByInstalacionId(Long instalacionId);

	InstalacionConfiguracionReservaDTO findDTOByInstalacionIdOrNewIfEmpty(Long instalacionId);
	


}
