package com.gestion.deportiva.service;

import java.util.List;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ProvinciaDTO;
import com.gestion.deportiva.dto.filter.ProvinciaFilter;

public interface ProvinciaService extends MaestraService<ProvinciaDTO, ProvinciaFilter> {
	
	List<ComboDTO> getListComboDTOByComunidadAutonomaUuid(String comunidadAutonomaUuid);
}
