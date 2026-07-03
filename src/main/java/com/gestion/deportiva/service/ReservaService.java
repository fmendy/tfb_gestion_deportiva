package com.gestion.deportiva.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gestion.deportiva.dto.ReservaListadoDTO;
import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;

import jakarta.validation.Valid;

public interface ReservaService extends BaseService<ReservaDTO, ReservaFilter> {

	boolean isFranjaHorariaDisponibleParaInstalacion(LocalDate fecha,LocalTime horaInicio, Long duracion,  Long instalacionId);

	boolean isFranjaHorariaDisponibleParaUsuario(LocalDate fecha,  LocalTime horaInicio, Long duracion, Long usuarioId);

	ReservaSolicitudDTO getFullReservaSolicitudDTOByReservaSolictudDTO(ReservaSolicitudDTO dto);

	Long crearReservaEstadoPendiente(@Valid ReservaSolicitudDTO dto);

	ReservaFilter getReservaFilterParaMisReservas();

	ReservaFilter getReservaFilterParaMisReservasPasadas();

	Page<ReservaListadoDTO> getPageMiReservaListadoDTOByFilter(ReservaFilter filter, Pageable pageable);

	boolean canEliminarReserva(Long reservaId);

	boolean canCancelarReservaPropia(Long reservaId);

	void cancelarPorUsuario(Long id);

	Page<ReservaListadoDTO> getPageListadoByFilter(ReservaFilter filter, Pageable pageable);

}
