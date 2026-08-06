package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.gestion.deportiva.dto.InstalacionConfiguracionReservaDTO;
import com.gestion.deportiva.dto.filter.InstalacionConfiguracionReservaFilter;
import com.gestion.deportiva.mapper.InstalacionConfiguracionReservaMapper;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;
import com.gestion.deportiva.repository.InstalacionConfiguracionReservaRepository;

@ExtendWith(MockitoExtension.class)
class InstalacionConfiguracionReservaServiceImplTest {

	@Mock
	private InstalacionConfiguracionReservaRepository instalacionConfiguracionReservaRepository;

	@Mock
	private InstalacionConfiguracionReservaMapper instalacionConfiguracionReservaMapper;

	@InjectMocks
	private InstalacionConfiguracionReservaServiceImpl instalacionConfiguracionReservaService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setId(id);
		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(instalacionConfiguracionReservaMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionConfiguracionReservaDTO resultado = instalacionConfiguracionReservaService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionConfiguracionReservaRepository).findByActivoTrueAndId(id);
		verify(instalacionConfiguracionReservaMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(instalacionConfiguracionReservaMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionConfiguracionReservaDTO resultado = instalacionConfiguracionReservaService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionConfiguracionReservaRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(instalacionConfiguracionReservaMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();
		dto.setUuid("uuid-nuevo");

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo"))
				.thenReturn(null);
		when(instalacionConfiguracionReservaMapper.dtoToModel(any(InstalacionConfiguracionReservaDTO.class),
				any(InstalacionConfiguracionReserva.class))).thenAnswer(invocation -> invocation.getArgument(1));

		Long id = instalacionConfiguracionReservaService.guardar(dto);

		verify(instalacionConfiguracionReservaRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(instalacionConfiguracionReservaRepository).saveAndFlush(any(InstalacionConfiguracionReserva.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();
		dto.setUuid("uuid-existente");

		InstalacionConfiguracionReserva modelExistente = new InstalacionConfiguracionReserva();
		modelExistente.setId(5L);

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(instalacionConfiguracionReservaMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = instalacionConfiguracionReservaService.guardar(dto);

		assertThat(id).isEqualTo(5L);
		verify(instalacionConfiguracionReservaRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		InstalacionConfiguracionReservaFilter filter = new InstalacionConfiguracionReservaFilter();
		Pageable pageable = PageRequest.of(0, 10);
		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		Page<InstalacionConfiguracionReserva> pageModel = new PageImpl<>(List.of(model));
		Page<InstalacionConfiguracionReservaDTO> pageDto = new PageImpl<>(
				List.of(new InstalacionConfiguracionReservaDTO()));

		when(instalacionConfiguracionReservaRepository.findAll(any(Specification.class), any(Pageable.class)))
				.thenReturn(pageModel);
		when(instalacionConfiguracionReservaMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<InstalacionConfiguracionReservaDTO> resultado = instalacionConfiguracionReservaService
				.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(instalacionConfiguracionReservaRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setActivo(true);

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndId(id)).thenReturn(model);

		instalacionConfiguracionReservaService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(instalacionConfiguracionReservaRepository).saveAndFlush(model);
	}

	@Test
	void obtenerListDTO() {
		List<InstalacionConfiguracionReserva> listaModel = List.of(new InstalacionConfiguracionReserva());
		List<InstalacionConfiguracionReservaDTO> listaDto = List.of(new InstalacionConfiguracionReservaDTO());

		when(instalacionConfiguracionReservaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(instalacionConfiguracionReservaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionConfiguracionReservaDTO> resultado = instalacionConfiguracionReservaService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(instalacionConfiguracionReservaRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		InstalacionConfiguracionReservaFilter filter = new InstalacionConfiguracionReservaFilter();
		List<InstalacionConfiguracionReserva> listaModel = List.of(new InstalacionConfiguracionReserva());
		List<InstalacionConfiguracionReservaDTO> listaDto = List.of(new InstalacionConfiguracionReservaDTO());

		when(instalacionConfiguracionReservaRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(instalacionConfiguracionReservaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionConfiguracionReservaDTO> resultado = instalacionConfiguracionReservaService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(instalacionConfiguracionReservaRepository).findAll(any(Specification.class));
	}

	@Test
	void canWriteYCanRead() {
		assertThat(instalacionConfiguracionReservaService.canWrite(1L)).isTrue();
		assertThat(instalacionConfiguracionReservaService.canRead(1L)).isTrue();
	}

	@Test
	void findDTOByInstalacionId() {
		Long instalacionId = 1L;
		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId))
				.thenReturn(model);
		when(instalacionConfiguracionReservaMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionConfiguracionReservaDTO resultado = instalacionConfiguracionReservaService
				.findDTOByInstalacionId(instalacionId);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionConfiguracionReservaRepository).findByActivoTrueAndInstalacionId(instalacionId);
	}

	@Test
	void findByInstalacionId() {
		Long instalacionId = 1L;
		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId))
				.thenReturn(model);

		InstalacionConfiguracionReserva resultado = instalacionConfiguracionReservaService
				.findByInstalacionId(instalacionId);

		assertThat(resultado).isEqualTo(model);
		verify(instalacionConfiguracionReservaRepository).findByActivoTrueAndInstalacionId(instalacionId);
	}

	@Test
	void findDTOByInstalacionIdOrNewIfEmptyCuandoExiste() {
		Long instalacionId = 1L;
		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId))
				.thenReturn(model);
		when(instalacionConfiguracionReservaMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionConfiguracionReservaDTO resultado = instalacionConfiguracionReservaService
				.findDTOByInstalacionIdOrNewIfEmpty(instalacionId);

		assertThat(resultado).isEqualTo(dto);
	}

	@Test
	void findDTOByInstalacionIdOrNewIfEmptyCuandoNoExiste() {
		Long instalacionId = 1L;

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId))
				.thenReturn(null);

		InstalacionConfiguracionReservaDTO resultado = instalacionConfiguracionReservaService
				.findDTOByInstalacionIdOrNewIfEmpty(instalacionId);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getInstalacionId()).isEqualTo(instalacionId);
	}

	@Test
	void isValidTrue() {
		Long instalacionId = 1L;
		LocalTime hora = LocalTime.of(10, 0);
		Long duracion = 60L;

		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setDuracionMin(30L);
		model.setDuracionMax(120L);
		model.setIntervaloHorario(30L);

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId))
				.thenReturn(model);

		boolean resultado = instalacionConfiguracionReservaService.isValid(instalacionId, hora, duracion);

		assertThat(resultado).isTrue();
	}

	@Test
	void isValidFalsePorDuracionMinima() {
		Long instalacionId = 1L;
		LocalTime hora = LocalTime.of(10, 0);
		Long duracion = 15L;

		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setDuracionMin(30L);
		model.setDuracionMax(120L);
		model.setIntervaloHorario(30L);

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId))
				.thenReturn(model);

		boolean resultado = instalacionConfiguracionReservaService.isValid(instalacionId, hora, duracion);

		assertThat(resultado).isFalse();
	}

	@Test
	void isValidFalsePorDuracionMaxima() {
		Long instalacionId = 1L;
		LocalTime hora = LocalTime.of(10, 0);
		Long duracion = 150L;

		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setDuracionMin(30L);
		model.setDuracionMax(120L);
		model.setIntervaloHorario(30L);

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId))
				.thenReturn(model);

		boolean resultado = instalacionConfiguracionReservaService.isValid(instalacionId, hora, duracion);

		assertThat(resultado).isFalse();
	}

	@Test
	void isValidFalsePorDuracionNoMultiploIntervalo() {
		Long instalacionId = 1L;
		LocalTime hora = LocalTime.of(10, 0);
		Long duracion = 45L;

		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setDuracionMin(30L);
		model.setDuracionMax(120L);
		model.setIntervaloHorario(30L);

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId))
				.thenReturn(model);

		boolean resultado = instalacionConfiguracionReservaService.isValid(instalacionId, hora, duracion);

		assertThat(resultado).isFalse();
	}

	@Test
	void isValidFalsePorHoraNoMultiploIntervalo() {
		Long instalacionId = 1L;
		LocalTime hora = LocalTime.of(10, 15);
		Long duracion = 60L;

		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setDuracionMin(30L);
		model.setDuracionMax(120L);
		model.setIntervaloHorario(30L);

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(instalacionId))
				.thenReturn(model);

		boolean resultado = instalacionConfiguracionReservaService.isValid(instalacionId, hora, duracion);

		assertThat(resultado).isFalse();
	}
}