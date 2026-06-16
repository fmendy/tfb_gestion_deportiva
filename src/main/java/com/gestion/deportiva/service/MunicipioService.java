package com.gestion.deportiva.service;

import java.util.List;


import com.gestion.deportiva.dto.MunicipioDTO;
import com.gestion.deportiva.dto.filter.MunicipioFilter;

public interface MunicipioService extends MaestraService<MunicipioDTO, MunicipioFilter> {

	List<MunicipioDTO> getListDTOByComunidadAutonomaIdOrProvinciaId(Long comunidadAutonomaId, Long provinciaId);
	


}
