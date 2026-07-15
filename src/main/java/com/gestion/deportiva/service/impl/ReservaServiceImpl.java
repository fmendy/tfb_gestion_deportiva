package com.gestion.deportiva.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ReservaListadoDTO;
import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;
import com.gestion.deportiva.dto.historico.HistoricoReservaDTO;
import com.gestion.deportiva.dto.specifications.ReservaSpecifications;
import com.gestion.deportiva.mapper.ReservaMapper;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionHorario;
import com.gestion.deportiva.model.InstalacionHorarioBloqueado;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.model.RevisionInfoEntity;
import com.gestion.deportiva.repository.InstalacionHorarioBloqueadoRepository;
import com.gestion.deportiva.repository.InstalacionHorarioEspecialRepository;
import com.gestion.deportiva.repository.InstalacionHorarioRepository;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.ReservaEstadoRepository;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.service.MailService;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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

	@Autowired
	private InstalacionHorarioBloqueadoRepository instalacionHorarioBloqueadoRepository;

	@Autowired
	private InstalacionHorarioEspecialRepository instalacionHorarioEspecialRepository;

	@Autowired
	private InstalacionHorarioRepository instalacionHorarioRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private UsuarioRepository usuarioRepository;

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
		return reservaMapper.pageToPageDTO(
				reservaRepository.findAll(ReservaSpecifications.filter(limitacionesPermisos(filter)), pageable));
	}

	@Override
	public Page<ReservaListadoDTO> getPageListadoByFilter(ReservaFilter filter, Pageable pageable) {
		return reservaMapper.pageToPageReservaListadoDTO(
				reservaRepository.findAll(ReservaSpecifications.filter(limitacionesPermisos(filter)), pageable));
	}

	private ReservaFilter limitacionesPermisos(ReservaFilter filter) {
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)
				|| SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA)) {
			return filter;
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA)) {
			filter.setListEmpresaIds(SecurityUtil.getCurrentUserListEmpresaId());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE)) {
			filter.setListSedeIds(SecurityUtil.getCurrentUserListSedeId());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION)) {
			filter.setListInstalacionIds(SecurityUtil.getCurrentUserListInstalacionId());
		} else {
			filter.setListInstalacionIds(List.of(-1L));
		}
		return filter;
	}

	@Override
	public Page<ReservaListadoDTO> getPageMiReservaListadoDTOByFilter(ReservaFilter filter, Pageable pageable) {
		return reservaMapper
				.pageToPageReservaListadoDTO(reservaRepository.findAll(ReservaSpecifications.filter(filter), pageable));
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
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)
				|| SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_GLOBAL)) {
			return true;
		}

		Reserva reserva = reservaRepository.findByActivoTrueAndId(id);
		if (reserva != null) {

			if (SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA)) {
				return SecurityUtil.getCurrentUserListEmpresaId()
						.contains(reserva.getInstalacion().getSede().getEmpresa().getId());
			}
			if (SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE)) {
				return SecurityUtil.getCurrentUserListSedeId().contains(reserva.getInstalacion().getSede().getId());
			}
			if (SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION)) {
				return SecurityUtil.getCurrentUserListInstalacionId().contains(reserva.getInstalacion().getId());
			}
			if (SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA)) {
				return reserva.getUsuarioCreacion().getId().equals(SecurityUtil.getCurrentUserId());
			}
		}
		return false;
	}

	@Override
	public boolean canRead(Long id) {
		return canWrite(id);
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
	@Transactional
	public Long crearReservaEstadoPendiente(@Valid ReservaSolicitudDTO dto) {
		Reserva reserva = new Reserva();
		reserva.setFecha(dto.getFecha());
		reserva.setHoraFin(dto.getHora().plusMinutes(dto.getDuracion()));
		reserva.setHoraInicio(dto.getHora());
		reserva.setInstalacion(instalacionRepository.findByActivoTrueAndId(dto.getInstalacionId()));
		reserva.setReservaEstado(
				reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.ReservaEstado.PENDIENTE));
		reservaRepository.save(reserva);
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
		return reserva.getUsuarioCreacion().getId().equals(SecurityUtil.getCurrentUserId())
				&& reserva.getReservaEstado().getNombre().equals(Constantes.ReservaEstado.PENDIENTE);
	}

	@Override
	public boolean canAprobarDenegarReserva(Long reservaId) {
		Reserva reserva = reservaRepository.findByActivoTrueAndId(reservaId);
		if (reserva != null && reserva.getReservaEstado().getNombre().equals(Constantes.ReservaEstado.PENDIENTE)) {
			return canWrite(reservaId);
		}
		return false;
	}

	@Override
	public boolean canCancelarUsuario(Long reservaId) {
		Reserva reserva = reservaRepository.findByActivoTrueAndId(reservaId);
		if (reserva == null) {
			return false;
		}
		return reserva.getUsuarioCreacion().getId().equals(SecurityUtil.getCurrentUserId())
				&& reserva.getReservaEstado().getNombre().equals(Constantes.ReservaEstado.APROBADA);
	}

	@Override
	public boolean canCancelarCompletadaIncompletadaEmpresa(Long reservaId) {
		Reserva reserva = reservaRepository.findByActivoTrueAndId(reservaId);
		if (reserva == null) {
			return false;
		}
		return reserva.getReservaEstado().getNombre().equals(Constantes.ReservaEstado.APROBADA) && canWrite(reservaId);
	}

	@Override
	public void cancelarUsuario(Long id) {
		actualizarReservaEstado(id, Constantes.ReservaEstado.CANCELADA_POR_USUARIO);
	}

	@Override
	public void aprobar(Long id) {
		actualizarReservaEstado(id, Constantes.ReservaEstado.APROBADA);
		try {
			mailService.mensajeAprobacionReserva(reservaRepository.findByActivoTrueAndId(id));
		} catch (MessagingException | IOException e) {
			logger.error("Se ha producido un error al enviar el mensaje de aprobacion de la reserva id {}", id);
		}
	}

	@Override
	public void completar(Long id) {
		actualizarReservaEstado(id, Constantes.ReservaEstado.COMPLETADA);
	}

	@Override
	public void incompletar(Long id) {
		actualizarReservaEstado(id, Constantes.ReservaEstado.INCOMPLETADA);
	}

	@Override
	public void cancelarSancion(Long id) {
		actualizarReservaEstado(id, Constantes.ReservaEstado.CANCELADA_POR_SANCION);
	}

	@Override
	public void cancelarSancion(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
		List<Reserva> reservas = reservaRepository
				.findByActivoTrueAndUsuarioCreacionIdAndFechaGreaterThanEqualAndFechaLessThanEqualAndReservaEstadoNombreIn(
						usuarioId, fechaInicio, fechaFin,
						Arrays.asList(Constantes.ReservaEstado.APROBADA, Constantes.ReservaEstado.PENDIENTE));
		for (Reserva reserva : reservas) {
			cancelarSancion(reserva.getId());
		}
	}

	@Override
	public void cancelarEmpresa(Long id) {
		actualizarReservaEstado(id, Constantes.ReservaEstado.CANCELADA_POR_EMPRESA);
		try {
			mailService.mensajeCanceladaEmpresaReserva(reservaRepository.findByActivoTrueAndId(id));
		} catch (MessagingException | IOException e) {
			logger.error("Error al notificar al usuario que la reserva ha sido cancelada, id reserva {}", id);
		}
	}

	@Override
	public void cancelarReservasEmpresa(List<Reserva> list) {
		for (Reserva reserva : list) {
			cancelarEmpresa(reserva.getId());
		}
	}

	@Override
	public void denegar(Long id) {
		actualizarReservaEstado(id, Constantes.ReservaEstado.DENEGADA);
		try {
			mailService.mensajeDenegacionReserva(reservaRepository.findByActivoTrueAndId(id));
		} catch (MessagingException | IOException e) {
			logger.error("Error al notificar al usuario que la reserva ha sido denegada, id reserva {}", id);
		}
	}

	private void actualizarReservaEstado(Long id, String nombreEstado) {
		Reserva reserva = reservaRepository.findByActivoTrueAndId(id);
		if (reserva == null) {
			throw new EntityNotFoundException("Reserva no encontrada");
		}

		ReservaEstado estado = reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombreEstado);
		reserva.setReservaEstado(estado);
		reservaRepository.saveAndFlush(reserva);
	}

	@Override
	public void fechaComprobarPorCambioDeHorarios(LocalDate date, Long instalacionId) {
		List<String> listReservaEstados = List.of(Constantes.ReservaEstado.PENDIENTE,
				Constantes.ReservaEstado.APROBADA);

		List<Reserva> reservas = reservaRepository.findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(
				date, instalacionId, listReservaEstados);

		if (reservas.isEmpty())
			return;

		// Obtención de datos
		List<InstalacionHorarioBloqueado> bloqueos = instalacionHorarioBloqueadoRepository
				.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, date);
		List<InstalacionHorarioEspecial> especiales = instalacionHorarioEspecialRepository
				.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, date);
		List<InstalacionHorario> normales = instalacionHorarioRepository
				.findByActivoTrueAndInstalacionIdAndDiaSemana(instalacionId, (long) date.getDayOfWeek().getValue());

		List<Reserva> reservasAfectadas = new ArrayList<>();

		if (especiales.stream().anyMatch(e -> e.getCerrado())) {
			reservasAfectadas.addAll(reservas);
		} else {
			for (Reserva reserva : reservas) {
				// 1. Siempre comprobamos si solapa con algún bloqueo (esto es prioridad
				// absoluta)
				boolean solapaConBloqueo = bloqueos.stream()
						.anyMatch(b -> reserva.getHoraInicio().isBefore(b.getHoraFin())
								&& reserva.getHoraFin().isAfter(b.getHoraInicio()));

				// 2. Comprobamos validez horaria (si está fuera de rango)
				boolean fueraDeRango = false;

				if (!especiales.isEmpty()) {
					// Existe horario especial: la reserva DEBE estar dentro de al menos uno
					boolean dentroDeEspecial = especiales.stream()
							.anyMatch(e -> !reserva.getHoraInicio().isBefore(e.getHoraInicio())
									&& !reserva.getHoraFin().isAfter(e.getHoraFin()));
					fueraDeRango = !dentroDeEspecial;
				} else {
					// No hay especial, comprobamos horario normal
					boolean dentroDeNormal = normales.stream()
							.anyMatch(n -> !reserva.getHoraInicio().isBefore(n.getHoraInicio())
									&& !reserva.getHoraFin().isAfter(n.getHoraFin()));
					fueraDeRango = !dentroDeNormal;
				}

				if (solapaConBloqueo || fueraDeRango) {
					reservasAfectadas.add(reserva);
				}
			}
		}
		cancelarReservasEmpresa(reservasAfectadas);

	}

	@Override
	public List<Reserva> getListByFechaDesdeInstalacionIdAndReservaEstados(LocalDate fechaDesde, Long instalacionId,
			List<String> listReservaEstados) {
		return reservaRepository.findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(fechaDesde,
				instalacionId, listReservaEstados);
	}
	
	@Override
	public List<Reserva> getListByFechaDesdeInstalacionSedeEmpresaIdAndReservaEstados(LocalDate fechaDesde, Long empresaId,
			List<String> listReservaEstados) {
		return reservaRepository.findByActivoTrueAndFechaAndInstalacionSedeEmpresaIdAndReservaEstadoNombreIn(fechaDesde,
				empresaId, listReservaEstados);
	}
	
	@Override
	public List<Reserva> getListByFechaDesdeInstalacionSedeIdAndReservaEstados(LocalDate fechaDesde, Long sedeId,
			List<String> listReservaEstados) {
		return reservaRepository.findByActivoTrueAndFechaAndInstalacionSedeIdAndReservaEstadoNombreIn(fechaDesde,
				sedeId, listReservaEstados);
	}

	@Override
	public List<HistoricoReservaDTO> getListHistorico(Long id) {
		AuditReader reader = AuditReaderFactory.get(entityManager);

		List<Number> revisiones = reader.getRevisions(Reserva.class, id);

		return revisiones.stream().sorted((a, b) -> b.intValue() - a.intValue()) // últimas revisiones primero
				.map(rev -> {
					Reserva entity = reader.find(Reserva.class, id, rev);
					RevisionInfoEntity revInfo = reader.findRevision(RevisionInfoEntity.class, rev);

					HistoricoReservaDTO dto = new HistoricoReservaDTO();
					dto.setId(entity.getId());
					dto.setFecha(entity.getFecha());
					dto.setHoraFin(entity.getHoraFin());
					dto.setHoraInicio(entity.getHoraInicio());
					Instalacion instalacion = instalacionRepository
							.findByActivoTrueAndId(entity.getInstalacion().getId());
					dto.setInstalacionInstalacionTipoNombre(instalacion.getInstalacionTipo().getNombre());
					dto.setInstalacionNombre(instalacion.getNombre());
					dto.setInstalacionSedeEmpresaNombre(instalacion.getSede().getEmpresa().getNombre());
					dto.setInstalacionSedeNombre(instalacion.getSede().getNombre());
					ReservaEstado reservaEstado = reservaEstadoRepository
							.findByActivoTrueAndId(entity.getReservaEstado().getId());
					dto.setReservaEstadoNombre(reservaEstado.getNombre());
					dto.setUsuarioModificacion(usuarioRepository.findById(revInfo.getUsuarioId())
							.map(u -> u.getNombre()).orElse("Desconocido"));
					dto.setFechaModificacion(new Date(revInfo.getRevtstmp()));
					return dto;
				}).toList();
	}
}
