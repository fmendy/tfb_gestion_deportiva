package com.gestion.deportiva.service;

import java.time.LocalDate;
import java.time.LocalTime;

import com.gestion.deportiva.dto.InstalacionHorarioEspecialDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioEspecialFilter;

public interface InstalacionHorarioEspecialService extends BaseService<InstalacionHorarioEspecialDTO, InstalacionHorarioEspecialFilter> {

	InstalacionHorarioEspecialDTO findByIdOrNewEmpty(Long id, Long instalacionId);
	
	Boolean estaAbierta(Long instalacionId, LocalDate fecha, LocalTime horaInicio, Long duracion);
	


}
