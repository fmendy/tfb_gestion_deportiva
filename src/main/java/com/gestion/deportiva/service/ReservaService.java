package com.gestion.deportiva.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gestion.deportiva.dto.ReservaListadoDTO;
import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;
import com.gestion.deportiva.dto.historico.HistoricoReservaDTO;
import com.gestion.deportiva.model.Reserva;

import jakarta.validation.Valid;

public interface ReservaService extends BaseService<ReservaDTO, ReservaFilter> {

	boolean isFranjaHorariaDisponibleParaInstalacion(LocalDate fecha, LocalTime horaInicio, Long duracion,
			Long instalacionId);

	boolean isFranjaHorariaDisponibleParaUsuario(LocalDate fecha, LocalTime horaInicio, Long duracion, Long usuarioId);

	ReservaSolicitudDTO getFullReservaSolicitudDTOByReservaSolictudDTO(ReservaSolicitudDTO dto);

	Long crearReservaEstadoPendiente(@Valid ReservaSolicitudDTO dto);

	ReservaFilter getReservaFilterParaMisReservas();

	ReservaFilter getReservaFilterParaMisReservasPasadas();

	Page<ReservaListadoDTO> getPageMiReservaListadoDTOByFilter(ReservaFilter filter, Pageable pageable);

	boolean canEliminarReserva(Long reservaId);

	boolean canCancelarUsuario(Long reservaId);

	void cancelarUsuario(Long id);

	Page<ReservaListadoDTO> getPageListadoByFilter(ReservaFilter filter, Pageable pageable);

	boolean canAprobarDenegarReserva(Long reservaId);

	void aprobar(Long id);

	void denegar(Long id);

	boolean canCancelarCompletadaIncompletadaEmpresa(Long reservaId);

	void completar(Long id);

	void incompletar(Long id);

	void cancelarEmpresa(Long id);

	void cancelarSancion(Long id);

	void cancelarSancion(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin);

	void fechaComprobarPorCambioDeHorarios(LocalDate date, Long instalacionId);

	void cancelarReservasEmpresa(List<Reserva> list);

	List<HistoricoReservaDTO> getListHistorico(Long id);

	List<Reserva> getListByFechaDesdeInstalacionIdAndReservaEstados(LocalDate fechaDesde, Long instalacionId,
			List<String> listReservaEstados);

	List<Reserva> getListByFechaDesdeInstalacionSedeEmpresaIdAndReservaEstados(LocalDate fechaDesde, Long empresaId,
			List<String> listReservaEstados);

	List<Reserva> getListByFechaDesdeInstalacionSedeIdAndReservaEstados(LocalDate fechaDesde, Long sedeId,
			List<String> listReservaEstados);

	void cancelarUsuarioFechaDesde(Long UsuarioId, LocalDate fechaDesde);

}
