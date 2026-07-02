package com.gestion.deportiva.service;

import java.time.LocalDate;
import java.time.LocalTime;

import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;

import jakarta.validation.Valid;

public interface ReservaService extends BaseService<ReservaDTO, ReservaFilter> {

	boolean isFranjaHorariaDisponibleParaInstalacion(LocalDate fecha,LocalTime horaInicio, Long duracion,  Long instalacionId);

	boolean isFranjaHorariaDisponibleParaUsuario(LocalDate fecha,  LocalTime horaInicio, Long duracion, Long usuarioId);

	ReservaSolicitudDTO getFullReservaSolicitudDTOByReservaSolictudDTO(ReservaSolicitudDTO dto);

	Long crearReservaEstadoPendiente(@Valid ReservaSolicitudDTO dto);

}
