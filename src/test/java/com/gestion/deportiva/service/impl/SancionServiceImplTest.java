package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
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

import com.gestion.deportiva.dto.CustomUserDetails;
import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.dto.filter.SancionFilter;
import com.gestion.deportiva.mapper.SancionMapper;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.Sancion;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.repository.SancionRepository;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class SancionServiceImplTest {

	@Mock
	private SancionRepository sancionRepository;

	@Mock
	private SancionMapper sancionMapper;

	@Mock
	private ReservaRepository reservaRepository;

	@Mock
	private ReservaService reservaService;

	@InjectMocks
	private SancionServiceImpl sancionService;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		securityUtilMockedStatic.close();
	}

	@Test
	void buscarPorId() {
		Long id = 1L;
		Sancion model = new Sancion();
		model.setId(id);
		SancionDTO dto = new SancionDTO();

		when(sancionRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(sancionMapper.modelToDTO(model)).thenReturn(dto);

		SancionDTO resultado = sancionService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(sancionRepository).findByActivoTrueAndId(id);
		verify(sancionMapper).modelToDTO(model);
	}

	@Test
	void buscarPorDTOConId() {
		SancionDTO dto = new SancionDTO();
		dto.setId(1L);
		Sancion model = new Sancion();
		SancionDTO expectedDto = new SancionDTO();

		when(sancionRepository.findByActivoTrueAndId(1L)).thenReturn(model);
		when(sancionMapper.modelToDTO(model)).thenReturn(expectedDto);

		SancionDTO resultado = sancionService.findByDTO(dto);

		assertThat(resultado).isEqualTo(expectedDto);
		verify(sancionRepository).findByActivoTrueAndId(1L);
	}

	@Test
	void buscarPorDTOSinId() {
		SancionDTO dto = new SancionDTO();
		dto.setReservaId(5L);
		Reserva reserva = new Reserva();
		SancionDTO expectedDto = new SancionDTO();

		when(reservaRepository.findByActivoTrueAndId(5L)).thenReturn(reserva);
		when(sancionMapper.dtoAndReservaToDTO(dto, reserva)).thenReturn(expectedDto);

		SancionDTO resultado = sancionService.findByDTO(dto);

		assertThat(resultado).isEqualTo(expectedDto);
		verify(reservaRepository).findByActivoTrueAndId(5L);
		verify(sancionMapper).dtoAndReservaToDTO(dto, reserva);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		Sancion model = new Sancion();
		SancionDTO dto = new SancionDTO();

		when(sancionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(sancionMapper.modelToDTO(model)).thenReturn(dto);

		SancionDTO resultado = sancionService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(sancionRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(sancionMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		SancionDTO dto = new SancionDTO();
		dto.setUuid("uuid-nuevo");
		dto.setUsuarioId(2L);
		dto.setFechaInicio(LocalDate.now(ZoneId.of("Europe/Madrid")));
		dto.setFechaFin(LocalDate.now(ZoneId.of("Europe/Madrid")).plusDays(5));

		when(sancionRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(sancionMapper.dtoToModel(any(SancionDTO.class), any(Sancion.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		sancionService.guardar(dto);

		verify(sancionRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(sancionRepository).saveAndFlush(any(Sancion.class));
		verify(reservaService).cancelarSancion(eq(2L), any(LocalDate.now(ZoneId.of("Europe/Madrid")).getClass()),
				any(LocalDate.now(ZoneId.of("Europe/Madrid")).getClass()));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		SancionDTO dto = new SancionDTO();
		dto.setUuid("uuid-existente");
		dto.setUsuarioId(2L);
		dto.setFechaInicio(LocalDate.now(ZoneId.of("Europe/Madrid")));
		dto.setFechaFin(LocalDate.now(ZoneId.of("Europe/Madrid")).plusDays(5));

		Sancion modelExistente = new Sancion();
		modelExistente.setId(10L);

		when(sancionRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente")).thenReturn(modelExistente);
		when(sancionMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = sancionService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(sancionRepository).saveAndFlush(modelExistente);
		verify(reservaService).cancelarSancion(eq(2L), any(LocalDate.now(ZoneId.of("Europe/Madrid")).getClass()),
				any(LocalDate.now(ZoneId.of("Europe/Madrid")).getClass()));
	}

	@Test
	void obtenerPaginaPorFiltro() {
		SancionFilter filter = new SancionFilter();
		Pageable pageable = PageRequest.of(0, 10);
		Sancion model = new Sancion();
		Page<Sancion> pageModel = new PageImpl<>(List.of(model));
		Page<SancionDTO> pageDto = new PageImpl<>(List.of(new SancionDTO()));

		when(sancionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(sancionMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<SancionDTO> resultado = sancionService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(sancionRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		Sancion model = new Sancion();
		model.setActivo(true);

		when(sancionRepository.findByActivoTrueAndId(id)).thenReturn(model);

		sancionService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(sancionRepository).saveAndFlush(model);
	}

	@Test
	void obtenerListDTO() {
		List<Sancion> listaModel = List.of(new Sancion());
		List<SancionDTO> listaDto = List.of(new SancionDTO());

		when(sancionRepository.findByActivoTrue()).thenReturn(listaModel);
		when(sancionMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<SancionDTO> resultado = sancionService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(sancionRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		SancionFilter filter = new SancionFilter();
		List<Sancion> listaModel = List.of(new Sancion());
		List<SancionDTO> listaDto = List.of(new SancionDTO());

		when(sancionRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(sancionMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<SancionDTO> resultado = sancionService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(sancionRepository).findAll(any(Specification.class));
	}

	@Test
	void canReadYCanWriteBasics() {
		Long sancionId = 1L;

		CustomUserDetails userDetails = new CustomUserDetails(1L, "usuario", "pass", Collections.emptySet(), List.of(),
				List.of(), List.of());

		securityUtilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(userDetails);
		when(sancionRepository.findByActivoTrueAndId(sancionId)).thenReturn(null);

		assertThat(sancionService.canRead(sancionId)).isFalse();
		assertThat(sancionService.canWrite(sancionId)).isFalse();
	}
}