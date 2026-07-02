package com.gestion.deportiva.service;

import java.time.LocalDate;
import java.time.LocalTime;

import com.gestion.deportiva.dto.InstalacionHorarioDTO;
import com.gestion.deportiva.dto.InstalacionHorarioSemanalDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioFilter;

import jakarta.validation.Valid;

public interface InstalacionHorarioService extends BaseService<InstalacionHorarioDTO, InstalacionHorarioFilter> {

	void guardar(@Valid InstalacionHorarioSemanalDTO dto);

	void borrarTodosLosHorarios(Long instalacionId);

	InstalacionHorarioSemanalDTO cargarHorarioSemanal(Long instalacionId);

	boolean estaAbierta(Long instalacionId, LocalDate fecha, LocalTime horaInicio, Long duracion);

}
