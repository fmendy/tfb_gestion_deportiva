package com.gestion.deportiva.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.MiReservaDTO;
import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;
import com.gestion.deportiva.dto.specifications.ReservaSpecifications;
import com.gestion.deportiva.mapper.ReservaMapper;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.ReservaEstadoRepository;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ReservaServiceImpl implements ReservaService {

	private static final Logger logger = LoggerFactory.getLogger(ReservaServiceImpl.class);

	@Autowired
	private ReservaRepository reservaRepository;

	@Autowired
	private InstalacionRepository instalacionRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ReservaMapper reservaMapper;

	@Autowired
	private ReservaEstadoRepository reservaEstadoRepository;

	@Override
	public ReservaDTO findById(Long id) {
		logger.info("Buscando Reserva por ID: {}", id);
		return reservaMapper.modelToDTO(reservaRepository.findByActivoTrueAndId(id));
	}

	@Override
	public ReservaDTO findByUuid(String uuid) {
		logger.info("Buscando Reserva por UUID: {}", uuid);
		return reservaMapper.modelToDTO(reservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(ReservaDTO dto) {
		logger.info("Guardando Reserva");
		Reserva model = reservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Reserva");
			model = new Reserva();
		}
		model = reservaMapper.dtoToModel(dto, model);
		reservaRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<ReservaDTO> getPageByFilter(ReservaFilter filter, Pageable pageable) {
		return reservaMapper.pageToPageDTO(reservaRepository.findAll(ReservaSpecifications.filter(filter), pageable));
	}
	
	@Override
	public Page<MiReservaDTO> getPageMiReservaDTOByFilter(ReservaFilter filter, Pageable pageable) {
		return reservaMapper.pageToPageMiReservaDTO(reservaRepository.findAll(ReservaSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando Reserva por ID: {}");
		Reserva model = reservaRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		reservaRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando Reserva por ID: {}");
		Reserva model = reservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		reservaRepository.saveAndFlush(model);
	}

	@Override
	public List<ReservaDTO> getListDTO() {
		return reservaMapper.listModelToListDTO(reservaRepository.findByActivoTrue());
	}

	@Override
	public List<ReservaDTO> getListDTO(ReservaFilter filter) {
		return reservaMapper.listModelToListDTO(reservaRepository.findAll(ReservaSpecifications.filter(filter)));
	}

	@Override
	public boolean canWrite(Long id) {
		return true;
	}

	@Override
	public boolean canRead(Long id) {
		return true;
	}

	@Override
	public byte[] exportarExcel(ReservaFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReservaSolicitudDTO getFullReservaSolicitudDTOByReservaSolictudDTO(ReservaSolicitudDTO dto) {
		Instalacion instalacion = instalacionRepository.findByActivoTrueAndId(dto.getInstalacionId());
		return reservaMapper.instalacionModelToReservaInstalacionDTO(instalacion, dto);
	}

	@Override
	public boolean isFranjaHorariaDisponibleParaInstalacion(LocalDate fecha, LocalTime horaInicio, Long duracion,
			Long instalacionId) {
		LocalTime horaFinSolicitada = horaInicio.plusMinutes(duracion);
		List<String> listReservaEstados = List.of(Constantes.ReservaEstado.PENDIENTE,
				Constantes.ReservaEstado.APROBADA);
		List<Reserva> reservas = reservaRepository.findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(
				fecha, instalacionId, listReservaEstados);
		boolean haySolapamiento = reservas.stream()
				.anyMatch(reserva -> reserva.getHoraInicio().isBefore(horaFinSolicitada)
						&& reserva.getHoraFin().isAfter(horaInicio));

		return !haySolapamiento;
	}

	@Override
	public boolean isFranjaHorariaDisponibleParaUsuario(LocalDate fecha, LocalTime horaInicio, Long duracion,
			Long usuarioId) {
		LocalTime horaFinSolicitada = horaInicio.plusMinutes(duracion);
		List<String> listReservaEstados = List.of(Constantes.ReservaEstado.PENDIENTE,
				Constantes.ReservaEstado.APROBADA);
		List<Reserva> reservas = reservaRepository.findByActivoTrueAndFechaAndUsuarioCreacionIdAndReservaEstadoNombreIn(
				fecha, usuarioId, listReservaEstados);
		boolean haySolapamiento = reservas.stream()
				.anyMatch(reserva -> reserva.getHoraInicio().isBefore(horaFinSolicitada)
						&& reserva.getHoraFin().isAfter(horaInicio));

		return !haySolapamiento;
	}

	@Override
	public Long crearReservaEstadoPendiente(@Valid ReservaSolicitudDTO dto) {
		Reserva reserva = new Reserva();
		reserva.setFecha(dto.getFecha());
		reserva.setHoraFin(dto.getHora().plusMinutes(dto.getDuracion()));
		reserva.setHoraInicio(dto.getHora());
		reserva.setInstalacion(new Instalacion(dto.getInstalacionId()));
		reserva.setReservaEstado(
				reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.ReservaEstado.PENDIENTE));
		reservaRepository.saveAndFlush(reserva);
		return reserva.getId();
	}

	@Override
	public ReservaFilter getReservaFilterParaMisReservas() {
		ReservaFilter filter = new ReservaFilter();
		filter.setUsuarioCreacionId(SecurityUtil.getCurrentUserId());
		filter.setFechaDesde(LocalDate.now());
		return filter;
	}
	
	@Override
	public ReservaFilter getReservaFilterParaMisReservasPasadas() {
		ReservaFilter filter = new ReservaFilter();
		filter.setUsuarioCreacionId(SecurityUtil.getCurrentUserId());
		filter.setFechaHasta(LocalDate.now().minusDays(1L));
		return filter;
	}
	
	@Override
	public boolean canEliminarReserva(Long reservaId) {
		Reserva reserva = reservaRepository.findByActivoTrueAndId(reservaId);
		if (reserva == null) {
			return false;
		}
		return reserva.getUsuarioCreacion().getId().equals(SecurityUtil.getCurrentUserId()) && reserva.getReservaEstado().getNombre().equals(Constantes.ReservaEstado.PENDIENTE);
	}
	
	@Override
	public boolean canCancelarReservaPropia(Long reservaId) {
		Reserva reserva = reservaRepository.findByActivoTrueAndId(reservaId);
		if (reserva == null) {
			return false;
		}
		return reserva.getUsuarioCreacion().getId().equals(SecurityUtil.getCurrentUserId()) && reserva.getReservaEstado().getNombre().equals(Constantes.ReservaEstado.APROBADA);
	}

	@Override
	public void cancelarPorUsuario(Long id) {
		Reserva reserva = reservaRepository.findByActivoTrueAndId(id);
		ReservaEstado estadoCancelado = reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.ReservaEstado.CANCELADA_POR_USUARIO);
		reserva.setReservaEstado(estadoCancelado);
		reservaRepository.saveAndFlush(reserva);
		
		
	}
	
}
