package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioBloqueadoFilter;
import com.gestion.deportiva.mapper.InstalacionHorarioBloqueadoMapper;
import com.gestion.deportiva.model.InstalacionHorarioBloqueado;
import com.gestion.deportiva.repository.InstalacionHorarioBloqueadoRepository;
import com.gestion.deportiva.service.ReservaService;

@ExtendWith(MockitoExtension.class)
class InstalacionHorarioBloqueadoServiceImplTest {

	@Mock
	private InstalacionHorarioBloqueadoRepository instalacionHorarioBloqueadoRepository;

	@Mock
	private InstalacionHorarioBloqueadoMapper instalacionHorarioBloqueadoMapper;

	@Mock
	private ReservaService reservaService;

	@InjectMocks
	private InstalacionHorarioBloqueadoServiceImpl instalacionHorarioBloqueadoService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		model.setId(id);
		InstalacionHorarioBloqueadoDTO dto = new InstalacionHorarioBloqueadoDTO();

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(instalacionHorarioBloqueadoMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionHorarioBloqueadoDTO resultado = instalacionHorarioBloqueadoService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionHorarioBloqueadoRepository).findByActivoTrueAndId(id);
		verify(instalacionHorarioBloqueadoMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		InstalacionHorarioBloqueadoDTO dto = new InstalacionHorarioBloqueadoDTO();

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(instalacionHorarioBloqueadoMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionHorarioBloqueadoDTO resultado = instalacionHorarioBloqueadoService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionHorarioBloqueadoRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(instalacionHorarioBloqueadoMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		InstalacionHorarioBloqueadoDTO dto = new InstalacionHorarioBloqueadoDTO();
		dto.setUuid("uuid-nuevo");
		dto.setFecha(LocalDate.of(2026, 6, 1));
		dto.setInstalacionId(5L);

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo"))
				.thenReturn(null);
		when(instalacionHorarioBloqueadoMapper.dtoToModel(any(InstalacionHorarioBloqueadoDTO.class),
				any(InstalacionHorarioBloqueado.class))).thenAnswer(invocation -> invocation.getArgument(1));

		Long id = instalacionHorarioBloqueadoService.guardar(dto);

		verify(instalacionHorarioBloqueadoRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(instalacionHorarioBloqueadoRepository).saveAndFlush(any(InstalacionHorarioBloqueado.class));
		verify(reservaService).fechaComprobarPorCambioDeHorarios(LocalDate.of(2026, 6, 1), 5L);
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		InstalacionHorarioBloqueadoDTO dto = new InstalacionHorarioBloqueadoDTO();
		dto.setUuid("uuid-existente");
		dto.setFecha(LocalDate.of(2026, 6, 1));
		dto.setInstalacionId(5L);

		InstalacionHorarioBloqueado modelExistente = new InstalacionHorarioBloqueado();
		modelExistente.setId(10L);

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(instalacionHorarioBloqueadoMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = instalacionHorarioBloqueadoService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(instalacionHorarioBloqueadoRepository).saveAndFlush(modelExistente);
		verify(reservaService).fechaComprobarPorCambioDeHorarios(LocalDate.of(2026, 6, 1), 5L);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		InstalacionHorarioBloqueadoFilter filter = new InstalacionHorarioBloqueadoFilter();
		Pageable pageable = PageRequest.of(0, 10);
		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		Page<InstalacionHorarioBloqueado> pageModel = new PageImpl<>(List.of(model));
		Page<InstalacionHorarioBloqueadoDTO> pageDto = new PageImpl<>(List.of(new InstalacionHorarioBloqueadoDTO()));

		when(instalacionHorarioBloqueadoRepository.findAll(any(Specification.class), any(Pageable.class)))
				.thenReturn(pageModel);
		when(instalacionHorarioBloqueadoMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<InstalacionHorarioBloqueadoDTO> resultado = instalacionHorarioBloqueadoService.getPageByFilter(filter,
				pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(instalacionHorarioBloqueadoRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		model.setActivo(true);

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndId(id)).thenReturn(model);

		instalacionHorarioBloqueadoService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(instalacionHorarioBloqueadoRepository).saveAndFlush(model);
	}

	@Test
	void eliminarPorUuid() {
		String uuid = "uuid-del";
		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		model.setActivo(true);

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);

		instalacionHorarioBloqueadoService.eliminar(uuid);

		assertThat(model.isActivo()).isFalse();
		verify(instalacionHorarioBloqueadoRepository).saveAndFlush(model);
	}

	@Test
	void obtenerListDTO() {
		List<InstalacionHorarioBloqueado> listaModel = List.of(new InstalacionHorarioBloqueado());
		List<InstalacionHorarioBloqueadoDTO> listaDto = List.of(new InstalacionHorarioBloqueadoDTO());

		when(instalacionHorarioBloqueadoRepository.findByActivoTrue()).thenReturn(listaModel);
		when(instalacionHorarioBloqueadoMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionHorarioBloqueadoDTO> resultado = instalacionHorarioBloqueadoService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(instalacionHorarioBloqueadoRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		InstalacionHorarioBloqueadoFilter filter = new InstalacionHorarioBloqueadoFilter();
		List<InstalacionHorarioBloqueado> listaModel = List.of(new InstalacionHorarioBloqueado());
		List<InstalacionHorarioBloqueadoDTO> listaDto = List.of(new InstalacionHorarioBloqueadoDTO());

		when(instalacionHorarioBloqueadoRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(instalacionHorarioBloqueadoMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionHorarioBloqueadoDTO> resultado = instalacionHorarioBloqueadoService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(instalacionHorarioBloqueadoRepository).findAll(any(Specification.class));
	}

	@Test
	void canWriteYCanRead() {
		assertThat(instalacionHorarioBloqueadoService.canWrite(1L)).isTrue();
		assertThat(instalacionHorarioBloqueadoService.canRead(1L)).isTrue();
	}

	@Test
	void findByIdOrNewEmptyCuandoIdNotNullYEiste() {
		Long id = 1L;
		Long instalacionId = 5L;
		InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
		InstalacionHorarioBloqueadoDTO dto = new InstalacionHorarioBloqueadoDTO();

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(instalacionHorarioBloqueadoMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionHorarioBloqueadoDTO resultado = instalacionHorarioBloqueadoService.findByIdOrNewEmpty(id,
				instalacionId);

		assertThat(resultado).isEqualTo(dto);
	}

	@Test
	void findByIdOrNewEmptyCuandoIdNullOosNoExiste() {
		Long id = null;
		Long instalacionId = 5L;

		InstalacionHorarioBloqueadoDTO resultado = instalacionHorarioBloqueadoService.findByIdOrNewEmpty(id,
				instalacionId);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getInstalacionId()).isEqualTo(instalacionId);
	}

	@Test
	void estaDisponibleTrueCuandoListaVacia() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1);
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L;

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, fecha))
				.thenReturn(List.of());

		boolean resultado = instalacionHorarioBloqueadoService.estaDisponible(instalacionId, fecha, horaInicio,
				duracion);

		assertThat(resultado).isTrue();
	}

	@Test
	void estaDisponibleTrueCuandoSinSolapamiento() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1);
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L; // 10:00 - 11:00

		InstalacionHorarioBloqueado bloque = new Inst_HorarioBloqueadoMockBuilder().horaInicio(LocalTime.of(8, 0))
				.horaFin(LocalTime.of(9, 0)).build();

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, fecha))
				.thenReturn(List.of(bloque));

		boolean resultado = instalacionHorarioBloqueadoService.estaDisponible(instalacionId, fecha, horaInicio,
				duracion);

		assertThat(resultado).isTrue();
	}

	@Test
	void estaDisponibleFalseCuandoHaySolapamiento() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1);
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L; // 10:00 - 11:00

		InstalacionHorarioBloqueado bloque = new Inst_HorarioBloqueadoMockBuilder().horaInicio(LocalTime.of(10, 30))
				.horaFin(LocalTime.of(11, 30)).build();

		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, fecha))
				.thenReturn(List.of(bloque));

		boolean resultado = instalacionHorarioBloqueadoService.estaDisponible(instalacionId, fecha, horaInicio,
				duracion);

		assertThat(resultado).isFalse();
	}

	private static class Inst_HorarioBloqueadoMockBuilder {
		private LocalTime horaInicio;
		private LocalTime horaFin;

		public Inst_HorarioBloqueadoMockBuilder horaInicio(LocalTime horaInicio) {
			this.horaInicio = horaInicio;
			return this;
		}

		public Inst_HorarioBloqueadoMockBuilder horaFin(LocalTime horaFin) {
			this.horaFin = horaFin;
			return this;
		}

		public InstalacionHorarioBloqueado build() {
			InstalacionHorarioBloqueado model = new InstalacionHorarioBloqueado();
			model.setHoraInicio(horaInicio);
			model.setHoraFin(horaFin);
			return model;
		}
	}
}