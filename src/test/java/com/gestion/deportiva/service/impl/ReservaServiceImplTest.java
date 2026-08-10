package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaListadoDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;
import com.gestion.deportiva.dto.historico.HistoricoReservaDTO;
import com.gestion.deportiva.mapper.ReservaMapper;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionHorarioBloqueado;
import com.gestion.deportiva.model.InstalacionTipo;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.model.RevisionInfoEntity;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.repository.InstalacionHorarioBloqueadoRepository;
import com.gestion.deportiva.repository.InstalacionHorarioEspecialRepository;
import com.gestion.deportiva.repository.InstalacionHorarioRepository;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.ReservaEstadoRepository;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.service.MailService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

	@Mock
	private ReservaRepository reservaRepository;

	@Mock
	private InstalacionRepository instalacionRepository;

	@Mock
	private EntityManager entityManager;

	@Mock
	private ReservaMapper reservaMapper;

	@Mock
	private ReservaEstadoRepository reservaEstadoRepository;

	@Mock
	private InstalacionHorarioBloqueadoRepository instalacionHorarioBloqueadoRepository;

	@Mock
	private InstalacionHorarioEspecialRepository instalacionHorarioEspecialRepository;

	@Mock
	private InstalacionHorarioRepository instalacionHorarioRepository;

	@Mock
	private MailService mailService;

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private ReservaServiceImpl reservaService;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@Mock
	private AuditReader auditReader;

	@BeforeEach
	void setUp() {
		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@org.junit.jupiter.api.AfterEach
	void tearDown() {
		if (securityUtilMockedStatic != null) {
			securityUtilMockedStatic.close();
		}
	}

	@Test
	void buscarPorId() {
		Long id = 1L;
		Reserva model = new Reserva();
		model.setId(id);
		ReservaDTO dto = new ReservaDTO();

		when(reservaRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(reservaMapper.modelToDTO(model)).thenReturn(dto);

		ReservaDTO resultado = reservaService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(reservaRepository).findByActivoTrueAndId(id);
		verify(reservaMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		Reserva model = new Reserva();
		ReservaDTO dto = new ReservaDTO();

		when(reservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(reservaMapper.modelToDTO(model)).thenReturn(dto);

		ReservaDTO resultado = reservaService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(reservaRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(reservaMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		ReservaDTO dto = new ReservaDTO();
		dto.setUuid("uuid-nuevo");

		when(reservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(reservaMapper.dtoToModel(any(ReservaDTO.class), any(Reserva.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		reservaService.guardar(dto);

		verify(reservaRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(reservaRepository).saveAndFlush(any(Reserva.class));
	}

	@Test
	void guardarExistenteКогдаYaExiste() {
		ReservaDTO dto = new ReservaDTO();
		dto.setUuid("uuid-existente");

		Reserva modelExistente = new Reserva();
		modelExistente.setId(10L);

		when(reservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente")).thenReturn(modelExistente);
		when(reservaMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = reservaService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(reservaRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaMiReservaListadoDTOByFilter() {
		ReservaFilter filter = new ReservaFilter();
		Pageable pageable = PageRequest.of(0, 10);
		Reserva model = new Reserva();
		Page<Reserva> pageModel = new PageImpl<>(List.of(model));
		Page<ReservaListadoDTO> pageDto = new PageImpl<>(List.of(new ReservaListadoDTO()));

		when(reservaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(reservaMapper.pageToPageReservaListadoDTO(pageModel)).thenReturn(pageDto);

		Page<ReservaListadoDTO> resultado = reservaService.getPageMiReservaListadoDTOByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		verify(reservaRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		Reserva model = new Reserva();
		model.setActivo(true);

		when(reservaRepository.findByActivoTrueAndId(id)).thenReturn(model);

		reservaService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(reservaRepository).saveAndFlush(model);
	}

	@Test
	void obtenerListDTO() {
		List<Reserva> listaModel = List.of(new Reserva());
		List<ReservaDTO> listaDto = List.of(new ReservaDTO());

		when(reservaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(reservaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<ReservaDTO> resultado = reservaService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(reservaRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		ReservaFilter filter = new ReservaFilter();
		List<Reserva> listaModel = List.of(new Reserva());
		List<ReservaDTO> listaDto = List.of(new ReservaDTO());

		when(reservaRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(reservaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<ReservaDTO> resultado = reservaService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(reservaRepository).findAll(any(Specification.class));
	}

	@Test
	void getFullReservaSolicitudDTOByReservaSolictudDTO() {
		ReservaSolicitudDTO solicitudDTO = new ReservaSolicitudDTO();
		solicitudDTO.setInstalacionId(1L);
		Instalacion instalacion = new Instalacion();
		ReservaSolicitudDTO expectedDto = new ReservaSolicitudDTO();

		when(instalacionRepository.findByActivoTrueAndId(1L)).thenReturn(instalacion);
		when(reservaMapper.instalacionModelToReservaInstalacionDTO(instalacion, solicitudDTO)).thenReturn(expectedDto);

		ReservaSolicitudDTO resultado = reservaService.getFullReservaSolicitudDTOByReservaSolictudDTO(solicitudDTO);

		assertThat(resultado).isEqualTo(expectedDto);
		verify(instalacionRepository).findByActivoTrueAndId(1L);
	}

	@Test
	void isFranjaHorariaDisponibleParaInstalacionSinSolapamiento() {
		LocalDate fecha = LocalDate.now(ZoneId.of("Europe/Madrid"));
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L;
		Long instalacionId = 1L;

		when(reservaRepository.findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(eq(fecha),
				eq(instalacionId), any())).thenReturn(List.of());

		boolean disponible = reservaService.isFranjaHorariaDisponibleParaInstalacion(fecha, horaInicio, duracion,
				instalacionId);

		assertThat(disponible).isTrue();
	}

	@Test
	void isFranjaHorariaDisponibleParaInstalacionConSolapamiento() {
		LocalDate fecha = LocalDate.now(ZoneId.of("Europe/Madrid"));
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L;
		Long instalacionId = 1L;

		Reserva reservaExistente = new Reserva();
		reservaExistente.setHoraInicio(LocalTime.of(10, 30));
		reservaExistente.setHoraFin(LocalTime.of(11, 30));

		when(reservaRepository.findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(eq(fecha),
				eq(instalacionId), any())).thenReturn(List.of(reservaExistente));

		boolean disponible = reservaService.isFranjaHorariaDisponibleParaInstalacion(fecha, horaInicio, duracion,
				instalacionId);

		assertThat(disponible).isFalse();
	}

	@Test
	void isFranjaHorariaDisponibleParaUsuarioSinSolapamiento() {
		LocalDate fecha = LocalDate.now(ZoneId.of("Europe/Madrid"));
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L;
		Long usuarioId = 1L;

		when(reservaRepository.findByActivoTrueAndFechaAndUsuarioCreacionIdAndReservaEstadoNombreIn(eq(fecha),
				eq(usuarioId), any())).thenReturn(List.of());

		boolean disponible = reservaService.isFranjaHorariaDisponibleParaUsuario(fecha, horaInicio, duracion,
				usuarioId);

		assertThat(disponible).isTrue();
	}

	@Test
	void crearReservaEstadoPendiente() {
		ReservaSolicitudDTO dto = new ReservaSolicitudDTO();
		dto.setFecha(LocalDate.now(ZoneId.of("Europe/Madrid")));
		dto.setHora(LocalTime.of(10, 0));
		dto.setDuracion(60L);
		dto.setInstalacionId(1L);

		Instalacion instalacion = new Instalacion();
		ReservaEstado estado = new ReservaEstado();

		when(instalacionRepository.findByActivoTrueAndId(1L)).thenReturn(instalacion);
		when(reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.ReservaEstado.PENDIENTE))
				.thenReturn(estado);
		when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
			Reserva r = invocation.getArgument(0);
			r.setId(5L);
			return r;
		});

		Long id = reservaService.crearReservaEstadoPendiente(dto);

		assertThat(id).isEqualTo(5L);
		verify(reservaRepository).save(any(Reserva.class));
	}

	@Test
	void getReservaFilterParaMisReservas() {
		ReservaFilter filter = reservaService.getReservaFilterParaMisReservas();
		assertThat(filter).isNotNull();
		assertThat(filter.getFechaDesde()).isEqualTo(LocalDate.now(ZoneId.of("Europe/Madrid")));
	}

	@Test
	void getReservaFilterParaMisReservasPasadas() {
		ReservaFilter filter = reservaService.getReservaFilterParaMisReservasPasadas();
		assertThat(filter).isNotNull();
		assertThat(filter.getFechaHasta()).isEqualTo(LocalDate.now(ZoneId.of("Europe/Madrid")).minusDays(1L));
	}

	@Test
	void actualizarReservaEstadoLanzaExcepcionSiNoExiste() {
		when(reservaRepository.findByActivoTrueAndId(99L)).thenReturn(null);

		assertThatThrownBy(() -> reservaService.cancelarUsuario(99L)).isInstanceOf(EntityNotFoundException.class)
				.hasMessage("Reserva no encontrada");
	}

	@Test
	void fechaComprobarPorCambioDeHorariosSinReservas() {
		LocalDate date = LocalDate.now(ZoneId.of("Europe/Madrid"));
		Long instalacionId = 1L;

		when(reservaRepository.findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(eq(date),
				eq(instalacionId), any())).thenReturn(List.of());

		reservaService.fechaComprobarPorCambioDeHorarios(date, instalacionId);

		verify(reservaRepository).findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(eq(date),
				eq(instalacionId), any());
	}

	@Test
	void isFranjaHorariaDisponibleParaUsuarioConSolapamiento() {
		LocalDate fecha = LocalDate.now(ZoneId.of("Europe/Madrid"));
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L;
		Long usuarioId = 1L;

		Reserva reservaExistente = new Reserva();
		reservaExistente.setHoraInicio(LocalTime.of(10, 30));
		reservaExistente.setHoraFin(LocalTime.of(11, 30));

		when(reservaRepository.findByActivoTrueAndFechaAndUsuarioCreacionIdAndReservaEstadoNombreIn(eq(fecha),
				eq(usuarioId), any())).thenReturn(List.of(reservaExistente));

		boolean disponible = reservaService.isFranjaHorariaDisponibleParaUsuario(fecha, horaInicio, duracion,
				usuarioId);

		assertThat(disponible).isFalse();
	}

	@Test
	void canEliminarReservaRetornaFalseSiNoExiste() {
		Long reservaId = 1L;

		when(reservaRepository.findByActivoTrueAndId(reservaId)).thenReturn(null);

		boolean resultado = reservaService.canEliminarReserva(reservaId);

		assertThat(resultado).isFalse();
	}

	@Test
	void canEliminarReservaEvaluaEntidadExistente() {
		Long reservaId = 1L;
		Reserva reserva = new Reserva();
		ReservaEstado estado = new ReservaEstado();
		estado.setNombre(Constantes.ReservaEstado.PENDIENTE);
		reserva.setReservaEstado(estado);

		com.gestion.deportiva.model.Usuario usuario = new com.gestion.deportiva.model.Usuario();
		usuario.setId(1L);
		reserva.setUsuarioCreacion(usuario);

		when(reservaRepository.findByActivoTrueAndId(reservaId)).thenReturn(reserva);

		reservaService.canEliminarReserva(reservaId);

		verify(reservaRepository).findByActivoTrueAndId(reservaId);
	}

	@Test
	void cancelarUsuarioFechaDesdeTest() {
		Long usuarioId = 1L;
		LocalDate fechaDesde = LocalDate.now();
		Reserva reserva = new Reserva();
		reserva.setId(1L);
		ReservaEstado estado = new ReservaEstado();
		estado.setNombre(Constantes.ReservaEstado.PENDIENTE);
		reserva.setReservaEstado(estado);

		when(reservaRepository.findByActivoTrueAndUsuarioCreacionIdAndFechaGreaterThanEqualAndReservaEstadoNombreIn(
				eq(usuarioId), eq(fechaDesde), any())).thenReturn(List.of(reserva));
		when(reservaRepository.findByActivoTrueAndId(1L)).thenReturn(reserva);
		when(reservaEstadoRepository
				.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.ReservaEstado.CANCELADA_POR_USUARIO))
				.thenReturn(estado);

		reservaService.cancelarUsuarioFechaDesde(usuarioId, fechaDesde);

		verify(reservaRepository).saveAndFlush(reserva);
	}

	@Test
	void aprobarReservaTest() throws MessagingException, IOException {
		Long reservaId = 1L;
		Reserva reserva = new Reserva();
		ReservaEstado estado = new ReservaEstado();
		reserva.setReservaEstado(estado);

		when(reservaRepository.findByActivoTrueAndId(reservaId)).thenReturn(reserva);
		when(reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.ReservaEstado.APROBADA))
				.thenReturn(estado);

		reservaService.aprobar(reservaId);

		verify(reservaRepository).saveAndFlush(reserva);
		verify(mailService).mensajeAprobacionReserva(reserva);
	}

	@Test
	void completarEIncompletarTest() {
		Long reservaId = 1L;
		Reserva reserva = new Reserva();
		ReservaEstado estado = new ReservaEstado();
		reserva.setReservaEstado(estado);

		when(reservaRepository.findByActivoTrueAndId(reservaId)).thenReturn(reserva);
		when(reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(any())).thenReturn(estado);

		reservaService.completar(reservaId);
		reservaService.incompletar(reservaId);

		verify(reservaRepository, org.mockito.Mockito.times(2)).saveAndFlush(reserva);
	}

	@Test
	void cancelarSancionRangoTest() {
		Long usuarioId = 1L;
		LocalDate inicio = LocalDate.now();
		LocalDate fin = LocalDate.now().plusDays(5);
		Reserva reserva = new Reserva();
		reserva.setId(1L);
		ReservaEstado estado = new ReservaEstado();
		estado.setNombre(Constantes.ReservaEstado.PENDIENTE);
		reserva.setReservaEstado(estado);

		when(reservaRepository
				.findByActivoTrueAndUsuarioCreacionIdAndFechaGreaterThanEqualAndFechaLessThanEqualAndReservaEstadoNombreIn(
						eq(usuarioId), eq(inicio), eq(fin), any()))
				.thenReturn(List.of(reserva));
		when(reservaRepository.findByActivoTrueAndId(1L)).thenReturn(reserva);
		when(reservaEstadoRepository
				.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.ReservaEstado.CANCELADA_POR_SANCION))
				.thenReturn(estado);

		reservaService.cancelarSancion(usuarioId, inicio, fin);

		verify(reservaRepository).saveAndFlush(reserva);
	}

	@Test
	void cancelarEmpresaYDenegarTest() throws MessagingException, IOException {
		Long reservaId = 1L;
		Reserva reserva = new Reserva();
		ReservaEstado estado = new ReservaEstado();
		reserva.setReservaEstado(estado);

		when(reservaRepository.findByActivoTrueAndId(reservaId)).thenReturn(reserva);
		when(reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(any())).thenReturn(estado);

		reservaService.cancelarEmpresa(reservaId);
		reservaService.denegar(reservaId);

		verify(mailService).mensajeCanceladaEmpresaReserva(reserva);
		verify(mailService).mensajeDenegacionReserva(reserva);
	}

	@Test
	void fechaComprobarPorCambioDeHorariosConEspecialCerrado() {
		LocalDate date = LocalDate.now();
		Long instalacionId = 1L;
		Reserva reserva = new Reserva();
		reserva.setId(1L);
		ReservaEstado estado = new ReservaEstado();
		reserva.setReservaEstado(estado);

		com.gestion.deportiva.model.InstalacionHorarioEspecial especial = new com.gestion.deportiva.model.InstalacionHorarioEspecial();
		especial.setCerrado(true);

		when(reservaRepository.findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(eq(date),
				eq(instalacionId), any())).thenReturn(List.of(reserva));
		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, date))
				.thenReturn(List.of());
		when(instalacionHorarioEspecialRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, date))
				.thenReturn(List.of(especial));
		when(reservaRepository.findByActivoTrueAndId(1L)).thenReturn(reserva);
		when(reservaEstadoRepository
				.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.ReservaEstado.CANCELADA_POR_EMPRESA))
				.thenReturn(estado);

		reservaService.fechaComprobarPorCambioDeHorarios(date, instalacionId);

		verify(reservaRepository).saveAndFlush(reserva);
	}

	@Test
	void getListByFechaMetodosTest() {
		LocalDate fecha = LocalDate.now();
		List<String> estados = List.of("PENDIENTE");
		when(reservaRepository.findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(fecha, 1L, estados))
				.thenReturn(List.of());
		when(reservaRepository.findByActivoTrueAndFechaAndInstalacionSedeEmpresaIdAndReservaEstadoNombreIn(fecha, 1L,
				estados)).thenReturn(List.of());
		when(reservaRepository.findByActivoTrueAndFechaAndInstalacionSedeIdAndReservaEstadoNombreIn(fecha, 1L, estados))
				.thenReturn(List.of());

		assertThat(reservaService.getListByFechaDesdeInstalacionIdAndReservaEstados(fecha, 1L, estados)).isEmpty();
		assertThat(reservaService.getListByFechaDesdeInstalacionSedeEmpresaIdAndReservaEstados(fecha, 1L, estados))
				.isEmpty();
		assertThat(reservaService.getListByFechaDesdeInstalacionSedeIdAndReservaEstados(fecha, 1L, estados)).isEmpty();
	}

	@Test
	void limitacionesPermisosYGetPageListadoByFilterTest() {
		ReservaFilter filter = new ReservaFilter();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Reserva> pageModel = new PageImpl<>(List.of(new Reserva()));
		Page<ReservaListadoDTO> pageDto = new PageImpl<>(List.of(new ReservaListadoDTO()));

		// 1. GESTION_GLOBAL
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL))
				.thenReturn(true);
		when(reservaRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageModel);
		when(reservaMapper.pageToPageReservaListadoDTO(pageModel)).thenReturn(pageDto);

		Page<ReservaListadoDTO> resultado = reservaService.getPageListadoByFilter(filter, pageable);
		assertThat(resultado).isNotNull();

		// 2. GESTION_RESERVA_EMPRESA
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL))
				.thenReturn(false);
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA))
				.thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListEmpresaId).thenReturn(List.of(1L));

		reservaService.getPageListadoByFilter(filter, pageable);

		// 3. GESTION_RESERVA_SEDE
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA))
				.thenReturn(false);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE))
				.thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListSedeId).thenReturn(List.of(1L));

		reservaService.getPageListadoByFilter(filter, pageable);

		// 4. GESTION_RESERVA_INSTALACION
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE))
				.thenReturn(false);
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION))
				.thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListInstalacionId).thenReturn(List.of(1L));

		reservaService.getPageListadoByFilter(filter, pageable);

		// 5. Sin permisos (Else)
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION))
				.thenReturn(false);
		reservaService.getPageListadoByFilter(filter, pageable);
	}

	@Test
	void canWriteYCanReadScenariosTest() {
		Long reservaId = 1L;
		Reserva reserva = new Reserva();
		Instalacion instalacion = new Instalacion();
		com.gestion.deportiva.model.Sede sede = new com.gestion.deportiva.model.Sede();
		com.gestion.deportiva.model.Empresa empresa = new com.gestion.deportiva.model.Empresa();
		empresa.setId(1L);
		sede.setEmpresa(empresa);
		sede.setId(2L);
		instalacion.setSede(sede);
		instalacion.setId(3L);
		reserva.setInstalacion(instalacion);

		com.gestion.deportiva.model.Usuario usuario = new com.gestion.deportiva.model.Usuario();
		usuario.setId(10L);
		reserva.setUsuarioCreacion(usuario);

		// GESTION_RESERVA_GLOBAL
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL))
				.thenReturn(false);
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_GLOBAL))
				.thenReturn(true);
		assertThat(reservaService.canWrite(reservaId)).isTrue();
		assertThat(reservaService.canRead(reservaId)).isTrue();

		// Reserva null
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_GLOBAL))
				.thenReturn(false);
		when(reservaRepository.findByActivoTrueAndId(reservaId)).thenReturn(null);
		assertThat(reservaService.canWrite(reservaId)).isFalse();

		// GESTION_RESERVA_EMPRESA match
		when(reservaRepository.findByActivoTrueAndId(reservaId)).thenReturn(reserva);
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA))
				.thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListEmpresaId).thenReturn(List.of(1L));
		assertThat(reservaService.canWrite(reservaId)).isTrue();

		// GESTION_RESERVA_SEDE match
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA))
				.thenReturn(false);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE))
				.thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListSedeId).thenReturn(List.of(2L));
		assertThat(reservaService.canWrite(reservaId)).isTrue();

		// GESTION_RESERVA_INSTALACION match
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE))
				.thenReturn(false);
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION))
				.thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListInstalacionId).thenReturn(List.of(3L));
		assertThat(reservaService.canWrite(reservaId)).isTrue();

		// GESTION_RESERVA_PROPIA match
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION))
				.thenReturn(false);
		securityUtilMockedStatic
				.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA))
				.thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(10L);
		assertThat(reservaService.canWrite(reservaId)).isTrue();
	}

	@Test
	void canAprobarDenegarYCancelacionesValidacionesTest() {
		Long reservaId = 1L;
		Reserva reserva = new Reserva();
		ReservaEstado estado = new ReservaEstado();
		estado.setNombre(Constantes.ReservaEstado.PENDIENTE);
		reserva.setReservaEstado(estado);

		com.gestion.deportiva.model.Usuario usuario = new com.gestion.deportiva.model.Usuario();
		usuario.setId(5L);
		reserva.setUsuarioCreacion(usuario);

		when(reservaRepository.findByActivoTrueAndId(reservaId)).thenReturn(reserva);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(any())).thenReturn(true);

		// canAprobarDenegarReserva
		assertThat(reservaService.canAprobarDenegarReserva(reservaId)).isTrue();

		// canCancelarUsuario (estado PENDIENTE -> false para cancelacion de usuario
		// aprobado, cambiamos a APROBADA)
		estado.setNombre(Constantes.ReservaEstado.APROBADA);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(5L);
		assertThat(reservaService.canCancelarUsuario(reservaId)).isTrue();

		// canCancelarCompletadaIncompletadaEmpresa
		assertThat(reservaService.canCancelarCompletadaIncompletadaEmpresa(reservaId)).isTrue();

		// Reserva null para cobertura de nulos
		when(reservaRepository.findByActivoTrueAndId(reservaId)).thenReturn(null);
		assertThat(reservaService.canCancelarUsuario(reservaId)).isFalse();
		assertThat(reservaService.canCancelarCompletadaIncompletadaEmpresa(reservaId)).isFalse();
		assertThat(reservaService.canAprobarDenegarReserva(reservaId)).isFalse();
	}

	@Test
	void determinarReservasAfectadasYFueraDeRangoTest() {
		LocalDate date = LocalDate.now();
		Long instalacionId = 1L;

		Reserva reserva = new Reserva();
		reserva.setHoraInicio(LocalTime.of(10, 0));
		reserva.setHoraFin(LocalTime.of(11, 0));

		// Bloqueo que solapa
		InstalacionHorarioBloqueado bloqueo = new InstalacionHorarioBloqueado();
		bloqueo.setHoraInicio(LocalTime.of(9, 30));
		bloqueo.setHoraFin(LocalTime.of(10, 30));

		when(reservaRepository.findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(eq(date),
				eq(instalacionId), any())).thenReturn(List.of(reserva));
		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, date))
				.thenReturn(List.of(bloqueo));
		when(instalacionHorarioEspecialRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, date))
				.thenReturn(List.of());
		when(instalacionHorarioRepository.findByActivoTrueAndInstalacionIdAndDiaSemana(any(), any()))
				.thenReturn(List.of());

		ReservaEstado estado = new ReservaEstado();
		when(reservaRepository.findByActivoTrueAndId(any())).thenReturn(reserva);
		when(reservaEstadoRepository
				.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.ReservaEstado.CANCELADA_POR_EMPRESA))
				.thenReturn(estado);

		reservaService.fechaComprobarPorCambioDeHorarios(date, instalacionId);
		verify(reservaRepository).saveAndFlush(any());
	}

}