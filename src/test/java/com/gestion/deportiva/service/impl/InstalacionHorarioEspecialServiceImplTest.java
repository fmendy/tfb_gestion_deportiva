package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
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

import com.gestion.deportiva.dto.InstalacionHorarioEspecialDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioEspecialFilter;
import com.gestion.deportiva.mapper.InstalacionHorarioEspecialMapper;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;
import com.gestion.deportiva.repository.InstalacionHorarioEspecialRepository;
import com.gestion.deportiva.service.ReservaService;

@ExtendWith(MockitoExtension.class)
class InstalacionHorarioEspecialServiceImplTest {

	@Mock
	private InstalacionHorarioEspecialRepository instalacionHorarioEspecialRepository;

	@Mock
	private InstalacionHorarioEspecialMapper instalacionHorarioEspecialMapper;

	@Mock
	private ReservaService reservaService;

	@InjectMocks
	private InstalacionHorarioEspecialServiceImpl instalacionHorarioEspecialService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		InstalacionHorarioEspecial model = new InstalacionHorarioEspecial();
		model.setId(id);
		InstalacionHorarioEspecialDTO dto = new InstalacionHorarioEspecialDTO();

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(instalacionHorarioEspecialMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionHorarioEspecialDTO resultado = instalacionHorarioEspecialService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionHorarioEspecialRepository).findByActivoTrueAndId(id);
		verify(instalacionHorarioEspecialMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		InstalacionHorarioEspecial model = new InstalacionHorarioEspecial();
		InstalacionHorarioEspecialDTO dto = new InstalacionHorarioEspecialDTO();

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(instalacionHorarioEspecialMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionHorarioEspecialDTO resultado = instalacionHorarioEspecialService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionHorarioEspecialRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(instalacionHorarioEspecialMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		InstalacionHorarioEspecialDTO dto = new InstalacionHorarioEspecialDTO();
		dto.setUuid("uuid-nuevo");
		dto.setFecha(LocalDate.of(2026, 6, 1));
		dto.setInstalacionId(5L);

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo"))
				.thenReturn(null);
		when(instalacionHorarioEspecialMapper.dtoToModel(any(InstalacionHorarioEspecialDTO.class),
				any(InstalacionHorarioEspecial.class))).thenAnswer(invocation -> invocation.getArgument(1));

		Long id = instalacionHorarioEspecialService.guardar(dto);

		verify(instalacionHorarioEspecialRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(instalacionHorarioEspecialRepository).saveAndFlush(any(InstalacionHorarioEspecial.class));
		verify(reservaService).fechaComprobarPorCambioDeHorarios(LocalDate.of(2026, 6, 1), 5L);
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		InstalacionHorarioEspecialDTO dto = new InstalacionHorarioEspecialDTO();
		dto.setUuid("uuid-existente");
		dto.setFecha(LocalDate.of(2026, 6, 1));
		dto.setInstalacionId(5L);

		InstalacionHorarioEspecial modelExistente = new InstalacionHorarioEspecial();
		modelExistente.setId(10L);

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(instalacionHorarioEspecialMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = instalacionHorarioEspecialService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(instalacionHorarioEspecialRepository).saveAndFlush(modelExistente);
		verify(reservaService).fechaComprobarPorCambioDeHorarios(LocalDate.of(2026, 6, 1), 5L);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		InstalacionHorarioEspecialFilter filter = new InstalacionHorarioEspecialFilter();
		Pageable pageable = PageRequest.of(0, 10);
		InstalacionHorarioEspecial model = new InstalacionHorarioEspecial();
		Page<InstalacionHorarioEspecial> pageModel = new PageImpl<>(List.of(model));
		Page<InstalacionHorarioEspecialDTO> pageDto = new PageImpl<>(List.of(new InstalacionHorarioEspecialDTO()));

		when(instalacionHorarioEspecialRepository.findAll(any(Specification.class), any(Pageable.class)))
				.thenReturn(pageModel);
		when(instalacionHorarioEspecialMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<InstalacionHorarioEspecialDTO> resultado = instalacionHorarioEspecialService.getPageByFilter(filter,
				pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(instalacionHorarioEspecialRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		InstalacionHorarioEspecial model = new InstalacionHorarioEspecial();
		model.setActivo(true);

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndId(id)).thenReturn(model);

		instalacionHorarioEspecialService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(instalacionHorarioEspecialRepository).saveAndFlush(model);
	}

	@Test
	void obtenerListDTO() {
		List<InstalacionHorarioEspecial> listaModel = List.of(new InstalacionHorarioEspecial());
		List<InstalacionHorarioEspecialDTO> listaDto = List.of(new InstalacionHorarioEspecialDTO());

		when(instalacionHorarioEspecialRepository.findByActivoTrue()).thenReturn(listaModel);
		when(instalacionHorarioEspecialMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionHorarioEspecialDTO> resultado = instalacionHorarioEspecialService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(instalacionHorarioEspecialRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		InstalacionHorarioEspecialFilter filter = new InstalacionHorarioEspecialFilter();
		List<InstalacionHorarioEspecial> listaModel = List.of(new InstalacionHorarioEspecial());
		List<InstalacionHorarioEspecialDTO> listaDto = List.of(new InstalacionHorarioEspecialDTO());

		when(instalacionHorarioEspecialRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(instalacionHorarioEspecialMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionHorarioEspecialDTO> resultado = instalacionHorarioEspecialService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(instalacionHorarioEspecialRepository).findAll(any(Specification.class));
	}

	@Test
	void canWriteYCanRead() {
		assertThat(instalacionHorarioEspecialService.canWrite(1L)).isTrue();
		assertThat(instalacionHorarioEspecialService.canRead(1L)).isTrue();
	}

	@Test
	void findByIdOrNewEmptyCuandoIdNotNullYEiste() {
		Long id = 1L;
		InstalacionHorarioEspecial model = new InstalacionHorarioEspecial();
		model.setId(id);
		InstalacionHorarioEspecialDTO dto = new InstalacionHorarioEspecialDTO();

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndId(id)).thenReturn(model);
		lenient().when(instalacionHorarioEspecialMapper.modelToDTO(model)).thenReturn(dto);
		lenient().when(instalacionHorarioEspecialMapper.modelToDTO(null)).thenReturn(null);

		InstalacionHorarioEspecialDTO resultado = instalacionHorarioEspecialService.findByIdOrNewEmpty(id, id);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionHorarioEspecialRepository).findByActivoTrueAndId(id);
	}

	@Test
	void findByIdOrNewEmptyCuandoIdNullOosNoExiste() {
		Long id = null;
		Long instalacionId = 5L;

		InstalacionHorarioEspecialDTO resultado = instalacionHorarioEspecialService.findByIdOrNewEmpty(id,
				instalacionId);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getInstalacionId()).isEqualTo(instalacionId);
	}

	@Test
	void estaAbiertaTrueCuandoListaVacia() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1);
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L;

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, fecha))
				.thenReturn(List.of());

		Boolean resultado = instalacionHorarioEspecialService.estaAbierta(instalacionId, fecha, horaInicio, duracion);

		assertThat(resultado).isTrue();
	}

	@Test
	void estaAbiertaFalseCuandoCerrado() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1);
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L;

		InstalacionHorarioEspecial model = new InstalacionHorarioEspecial();
		model.setCerrado(true);

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, fecha))
				.thenReturn(List.of(model));

		Boolean resultado = instalacionHorarioEspecialService.estaAbierta(instalacionId, fecha, horaInicio, duracion);

		assertThat(resultado).isFalse();
	}

	@Test
	void estaAbiertaTrueCuandoDentroDeRango() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1);
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L; // 10:00 - 11:00

		InstalacionHorarioEspecial model = new InstalacionHorarioEspecial();
		model.setCerrado(false);
		model.setHoraInicio(LocalTime.of(9, 0));
		model.setHoraFin(LocalTime.of(12, 0));

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, fecha))
				.thenReturn(List.of(model));

		Boolean resultado = instalacionHorarioEspecialService.estaAbierta(instalacionId, fecha, horaInicio, duracion);

		assertThat(resultado).isTrue();
	}

	@Test
	void estaAbiertaFalseCuandoFueraDeRango() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1);
		LocalTime horaInicio = LocalTime.of(8, 0);
		Long duracion = 60L; // 08:00 - 09:00

		InstalacionHorarioEspecial model = new InstalacionHorarioEspecial();
		model.setCerrado(false);
		model.setHoraInicio(LocalTime.of(9, 0));
		model.setHoraFin(LocalTime.of(12, 0));

		when(instalacionHorarioEspecialRepository.findByActivoTrueAndInstalacionIdAndFecha(instalacionId, fecha))
				.thenReturn(List.of(model));

		Boolean resultado = instalacionHorarioEspecialService.estaAbierta(instalacionId, fecha, horaInicio, duracion);

		assertThat(resultado).isFalse();
	}
}