package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.gestion.deportiva.dto.InstalacionHorarioDTO;
import com.gestion.deportiva.dto.InstalacionHorarioSemanalDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioFilter;
import com.gestion.deportiva.mapper.InstalacionHorarioMapper;
import com.gestion.deportiva.model.InstalacionHorario;
import com.gestion.deportiva.repository.InstalacionHorarioRepository;
import com.gestion.deportiva.service.ReservaService;

@ExtendWith(MockitoExtension.class)
class InstalacionHorarioServiceImplTest {

	@Mock
	private InstalacionHorarioRepository instalacionHorarioRepository;

	@Mock
	private InstalacionHorarioMapper instalacionHorarioMapper;

	@Mock
	private ReservaService reservaService;

	@InjectMocks
	private InstalacionHorarioServiceImpl instalacionHorarioService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		InstalacionHorario model = new InstalacionHorario();
		model.setId(id);
		InstalacionHorarioDTO dto = new InstalacionHorarioDTO();

		when(instalacionHorarioRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(instalacionHorarioMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionHorarioDTO resultado = instalacionHorarioService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionHorarioRepository).findByActivoTrueAndId(id);
		verify(instalacionHorarioMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		InstalacionHorario model = new InstalacionHorario();
		InstalacionHorarioDTO dto = new InstalacionHorarioDTO();

		when(instalacionHorarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(instalacionHorarioMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionHorarioDTO resultado = instalacionHorarioService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionHorarioRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(instalacionHorarioMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		InstalacionHorarioDTO dto = new InstalacionHorarioDTO();
		dto.setUuid("uuid-nuevo");

		when(instalacionHorarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(instalacionHorarioMapper.dtoToModel(any(InstalacionHorarioDTO.class), any(InstalacionHorario.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		instalacionHorarioService.guardar(dto);

		verify(instalacionHorarioRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(instalacionHorarioRepository).saveAndFlush(any(InstalacionHorario.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		InstalacionHorarioDTO dto = new InstalacionHorarioDTO();
		dto.setUuid("uuid-existente");

		InstalacionHorario modelExistente = new InstalacionHorario();
		modelExistente.setId(10L);

		when(instalacionHorarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(instalacionHorarioMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = instalacionHorarioService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(instalacionHorarioRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		InstalacionHorarioFilter filter = new InstalacionHorarioFilter();
		Pageable pageable = PageRequest.of(0, 10);
		InstalacionHorario model = new InstalacionHorario();
		Page<InstalacionHorario> pageModel = new PageImpl<>(List.of(model));
		Page<InstalacionHorarioDTO> pageDto = new PageImpl<>(List.of(new InstalacionHorarioDTO()));

		when(instalacionHorarioRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(instalacionHorarioMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<InstalacionHorarioDTO> resultado = instalacionHorarioService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(instalacionHorarioRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		InstalacionHorario model = new InstalacionHorario();
		model.setActivo(true);

		when(instalacionHorarioRepository.findByActivoTrueAndId(id)).thenReturn(model);

		instalacionHorarioService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(instalacionHorarioRepository).saveAndFlush(model);
	}

	@Test
	void obtenerListDTO() {
		List<InstalacionHorario> listaModel = List.of(new InstalacionHorario());
		List<InstalacionHorarioDTO> listaDto = List.of(new InstalacionHorarioDTO());

		when(instalacionHorarioRepository.findByActivoTrue()).thenReturn(listaModel);
		when(instalacionHorarioMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionHorarioDTO> resultado = instalacionHorarioService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(instalacionHorarioRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		InstalacionHorarioFilter filter = new InstalacionHorarioFilter();
		List<InstalacionHorario> listaModel = List.of(new InstalacionHorario());
		List<InstalacionHorarioDTO> listaDto = List.of(new InstalacionHorarioDTO());

		when(instalacionHorarioRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(instalacionHorarioMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionHorarioDTO> resultado = instalacionHorarioService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(instalacionHorarioRepository).findAll(any(Specification.class));
	}

	@Test
	void canWriteYCanRead() {
		assertThat(instalacionHorarioService.canWrite(1L)).isTrue();
		assertThat(instalacionHorarioService.canRead(1L)).isTrue();
	}

	@Test
	void borrarTodosLosHorarios() {
		Long instalacionId = 1L;
		InstalacionHorario model = new InstalacionHorario();
		model.setActivo(true);

		when(instalacionHorarioRepository.findByActivoTrueAndInstalacionId(instalacionId)).thenReturn(List.of(model));

		instalacionHorarioService.borrarTodosLosHorarios(instalacionId);

		assertThat(model.isActivo()).isFalse();
		verify(instalacionHorarioRepository).saveAllAndFlush(any());
	}

	@Test
	void guardarHorarioSemanal() {
		Long instalacionId = 1L;
		InstalacionHorarioSemanalDTO dtoSemanal = new InstalacionHorarioSemanalDTO();
		dtoSemanal.setInstalacionId(instalacionId);

		InstalacionHorarioDTO turnoDto = new InstalacionHorarioDTO();
		turnoDto.setHoraInicio(LocalTime.of(8, 0));
		turnoDto.setHoraFin(LocalTime.of(12, 0));

		dtoSemanal.getHorarios().put(1, List.of(turnoDto));

		when(instalacionHorarioRepository.findByActivoTrueAndInstalacionId(instalacionId)).thenReturn(List.of());

		instalacionHorarioService.guardar(dtoSemanal);

		verify(instalacionHorarioRepository).saveAllAndFlush(any());
		verify(instalacionHorarioRepository).save(any(InstalacionHorario.class));
	}

	@Test
	void cargarHorarioSemanal() {
		Long instalacionId = 1L;
		InstalacionHorario model = new InstalacionHorario();
		model.setId(10L);
		model.setDiaSemana(1L);
		model.setHoraInicio(LocalTime.of(8, 0));
		model.setHoraFin(LocalTime.of(12, 0));

		when(instalacionHorarioRepository.findByActivoTrueAndInstalacionId(instalacionId)).thenReturn(List.of(model));

		InstalacionHorarioSemanalDTO resultado = instalacionHorarioService.cargarHorarioSemanal(instalacionId);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getInstalacionId()).isEqualTo(instalacionId);
		assertThat(resultado.getHorarios().get(1)).hasSize(1);
	}

	@Test
	void estaAbiertaFalseCuandoListaVacia() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1); // Lunes
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L;

		when(instalacionHorarioRepository.findByActivoTrueAndInstalacionIdAndDiaSemana(eq(instalacionId), any()))
				.thenReturn(List.of());

		boolean resultado = instalacionHorarioService.estaAbierta(instalacionId, fecha, horaInicio, duracion);

		assertThat(resultado).isFalse();
	}

	@Test
	void estaAbiertaTrueCuandoDentroDeRango() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1);
		LocalTime horaInicio = LocalTime.of(10, 0);
		Long duracion = 60L; // 10:00 - 11:00

		InstalacionHorario model = new InstalacionHorario();
		model.setHoraInicio(LocalTime.of(9, 0));
		model.setHoraFin(LocalTime.of(12, 0));

		when(instalacionHorarioRepository.findByActivoTrueAndInstalacionIdAndDiaSemana(eq(instalacionId), any()))
				.thenReturn(List.of(model));

		boolean resultado = instalacionHorarioService.estaAbierta(instalacionId, fecha, horaInicio, duracion);

		assertThat(resultado).isTrue();
	}

	@Test
	void estaAbiertaFalseCuandoFueraDeRango() {
		Long instalacionId = 1L;
		LocalDate fecha = LocalDate.of(2026, 6, 1);
		LocalTime horaInicio = LocalTime.of(8, 0);
		Long duracion = 60L; // 08:00 - 09:00

		InstalacionHorario model = new InstalacionHorario();
		model.setHoraInicio(LocalTime.of(9, 0));
		model.setHoraFin(LocalTime.of(12, 0));

		when(instalacionHorarioRepository.findByActivoTrueAndInstalacionIdAndDiaSemana(eq(instalacionId), any()))
				.thenReturn(List.of(model));

		boolean resultado = instalacionHorarioService.estaAbierta(instalacionId, fecha, horaInicio, duracion);

		assertThat(resultado).isFalse();
	}
}