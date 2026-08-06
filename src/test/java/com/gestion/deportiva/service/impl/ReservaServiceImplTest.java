package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.gestion.deportiva.mapper.ReservaMapper;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.repository.InstalacionHorarioBloqueadoRepository;
import com.gestion.deportiva.repository.InstalacionHorarioEspecialRepository;
import com.gestion.deportiva.repository.InstalacionHorarioRepository;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.ReservaEstadoRepository;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.service.MailService;
import com.gestion.deportiva.util.Constantes;

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
	void guardarExistenteCuandoYaExiste() {
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
}