package com.gestion.deportiva.service;

import java.time.LocalDate;
import java.time.LocalTime;

import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioBloqueadoFilter;

public interface InstalacionHorarioBloqueadoService extends BaseService<InstalacionHorarioBloqueadoDTO, InstalacionHorarioBloqueadoFilter> {

	InstalacionHorarioBloqueadoDTO findByIdOrNewEmpty(Long id, Long instalacionId);

	boolean estaDisponible(Long instalacionId, LocalDate fecha, LocalTime hora, Long duracion);
	


}
