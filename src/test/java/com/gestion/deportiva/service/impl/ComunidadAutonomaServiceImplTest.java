package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ComunidadAutonomaDTO;
import com.gestion.deportiva.dto.filter.ComunidadAutonomaFilter;
import com.gestion.deportiva.mapper.ComunidadAutonomaMapper;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.repository.ComunidadAutonomaRepository;

@ExtendWith(MockitoExtension.class)
class ComunidadAutonomaServiceImplTest {

	@Mock
	private ComunidadAutonomaRepository comunidadAutonomaRepository;

	@Mock
	private ComunidadAutonomaMapper comunidadAutonomaMapper;

	@InjectMocks
	private ComunidadAutonomaServiceImpl comunidadAutonomaService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		ComunidadAutonoma model = new ComunidadAutonoma();
		model.setId(id);
		ComunidadAutonomaDTO dto = new ComunidadAutonomaDTO();

		when(comunidadAutonomaRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(comunidadAutonomaMapper.modelToDTO(model)).thenReturn(dto);

		ComunidadAutonomaDTO resultado = comunidadAutonomaService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(comunidadAutonomaRepository).findByActivoTrueAndId(id);
		verify(comunidadAutonomaMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		ComunidadAutonoma model = new ComunidadAutonoma();
		ComunidadAutonomaDTO dto = new ComunidadAutonomaDTO();

		when(comunidadAutonomaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(comunidadAutonomaMapper.modelToDTO(model)).thenReturn(dto);

		ComunidadAutonomaDTO resultado = comunidadAutonomaService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(comunidadAutonomaRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(comunidadAutonomaMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		ComunidadAutonomaDTO dto = new ComunidadAutonomaDTO();
		dto.setUuid("uuid-nuevo");

		when(comunidadAutonomaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(comunidadAutonomaMapper.dtoToModel(any(ComunidadAutonomaDTO.class), any(ComunidadAutonoma.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		comunidadAutonomaService.guardar(dto);

		verify(comunidadAutonomaRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(comunidadAutonomaRepository).saveAndFlush(any(ComunidadAutonoma.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		ComunidadAutonomaDTO dto = new ComunidadAutonomaDTO();
		dto.setUuid("uuid-existente");

		ComunidadAutonoma modelExistente = new ComunidadAutonoma();
		modelExistente.setId(5L);

		when(comunidadAutonomaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(comunidadAutonomaMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = comunidadAutonomaService.guardar(dto);

		assertThat(id).isEqualTo(5L);
		verify(comunidadAutonomaRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		ComunidadAutonomaFilter filter = new ComunidadAutonomaFilter();
		Pageable pageable = PageRequest.of(0, 10);
		ComunidadAutonoma model = new ComunidadAutonoma();
		Page<ComunidadAutonoma> pageModel = new PageImpl<>(List.of(model));
		Page<ComunidadAutonomaDTO> pageDto = new PageImpl<>(List.of(new ComunidadAutonomaDTO()));

		when(comunidadAutonomaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(comunidadAutonomaMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<ComunidadAutonomaDTO> resultado = comunidadAutonomaService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(comunidadAutonomaRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		ComunidadAutonoma model = new ComunidadAutonoma();
		model.setActivo(true);

		when(comunidadAutonomaRepository.findByActivoTrueAndId(id)).thenReturn(model);

		comunidadAutonomaService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(comunidadAutonomaRepository).saveAndFlush(model);
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Madrid";
		ComunidadAutonoma model = new ComunidadAutonoma();
		ComunidadAutonomaDTO dto = new ComunidadAutonomaDTO();

		when(comunidadAutonomaRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(model);
		when(comunidadAutonomaMapper.modelToDTO(model)).thenReturn(dto);

		ComunidadAutonomaDTO resultado = comunidadAutonomaService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(comunidadAutonomaRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
	}

	@Test
	void obtenerListComboDTO() {
		List<ComunidadAutonoma> listaModel = List.of(new ComunidadAutonoma());
		List<ComboDTO> listaCombo = List.of(new ComboDTO());

		when(comunidadAutonomaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(comunidadAutonomaMapper.listModelToListComboDTO(listaModel)).thenReturn(listaCombo);

		List<ComboDTO> resultado = comunidadAutonomaService.getListComboDTO();

		assertThat(resultado).isEqualTo(listaCombo);
		verify(comunidadAutonomaRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTO() {
		List<ComunidadAutonoma> listaModel = List.of(new ComunidadAutonoma());
		List<ComunidadAutonomaDTO> listaDto = List.of(new ComunidadAutonomaDTO());

		when(comunidadAutonomaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(comunidadAutonomaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);


		verify(comunidadAutonomaRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		ComunidadAutonomaFilter filter = new ComunidadAutonomaFilter();
		List<ComunidadAutonoma> listaModel = List.of(new ComunidadAutonoma());
		List<ComunidadAutonomaDTO> listaDto = List.of(new ComunidadAutonomaDTO());

		when(comunidadAutonomaRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(comunidadAutonomaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);


		verify(comunidadAutonomaRepository).findAll(any(Specification.class));
	}

	@Test
	void validarPermisosEscrituraYLectura() {
		assertThat(comunidadAutonomaService.canWrite(1L)).isTrue();
		assertThat(comunidadAutonomaService.canRead(1L)).isTrue();
	}
}