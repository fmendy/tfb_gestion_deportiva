package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.gestion.deportiva.dto.SancionTipoDTO;
import com.gestion.deportiva.dto.filter.SancionTipoFilter;
import com.gestion.deportiva.mapper.SancionTipoMapper;
import com.gestion.deportiva.model.SancionTipo;
import com.gestion.deportiva.repository.SancionTipoRepository;

@ExtendWith(MockitoExtension.class)
class SancionTipoServiceImplTest {

	@Mock
	private SancionTipoRepository sancionTipoRepository;

	@Mock
	private SancionTipoMapper sancionTipoMapper;

	@InjectMocks
	private SancionTipoServiceImpl sancionTipoService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		SancionTipo model = new SancionTipo();
		model.setId(id);
		SancionTipoDTO dto = new SancionTipoDTO();

		when(sancionTipoRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(sancionTipoMapper.modelToDTO(model)).thenReturn(dto);

		SancionTipoDTO resultado = sancionTipoService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(sancionTipoRepository).findByActivoTrueAndId(id);
		verify(sancionTipoMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		SancionTipo model = new SancionTipo();
		SancionTipoDTO dto = new SancionTipoDTO();

		when(sancionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(sancionTipoMapper.modelToDTO(model)).thenReturn(dto);

		SancionTipoDTO resultado = sancionTipoService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(sancionTipoRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(sancionTipoMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		SancionTipoDTO dto = new SancionTipoDTO();
		dto.setUuid("uuid-nuevo");

		when(sancionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(sancionTipoMapper.dtoToModel(any(SancionTipoDTO.class), any(SancionTipo.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		sancionTipoService.guardar(dto);

		verify(sancionTipoRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(sancionTipoRepository).saveAndFlush(any(SancionTipo.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		SancionTipoDTO dto = new SancionTipoDTO();
		dto.setUuid("uuid-existente");

		SancionTipo modelExistente = new SancionTipo();
		modelExistente.setId(10L);

		when(sancionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(sancionTipoMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = sancionTipoService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(sancionTipoRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		SancionTipoFilter filter = new SancionTipoFilter();
		Pageable pageable = PageRequest.of(0, 10);
		SancionTipo model = new SancionTipo();
		Page<SancionTipo> pageModel = new PageImpl<>(List.of(model));
		Page<SancionTipoDTO> pageDto = new PageImpl<>(List.of(new SancionTipoDTO()));

		when(sancionTipoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(sancionTipoMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<SancionTipoDTO> resultado = sancionTipoService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(sancionTipoRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		SancionTipo model = new SancionTipo();
		model.setActivo(true);

		when(sancionTipoRepository.findByActivoTrueAndId(id)).thenReturn(model);

		sancionTipoService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(sancionTipoRepository).saveAndFlush(model);
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Leve";
		SancionTipo model = new SancionTipo();
		SancionTipoDTO dto = new SancionTipoDTO();

		when(sancionTipoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(model);
		when(sancionTipoMapper.modelToDTO(model)).thenReturn(dto);

		SancionTipoDTO resultado = sancionTipoService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(sancionTipoRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
		verify(sancionTipoMapper).modelToDTO(model);
	}

	@Test
	void obtenerListComboDTO() {
		List<SancionTipo> listaModel = List.of(new SancionTipo());
		List<ComboDTO> listaComboDto = List.of(new ComboDTO());

		when(sancionTipoRepository.findByActivoTrue()).thenReturn(listaModel);
		when(sancionTipoMapper.listModelToListComboDTO(listaModel)).thenReturn(listaComboDto);

		List<ComboDTO> resultado = sancionTipoService.getListComboDTO();

		assertThat(resultado).isNotNull();
		verify(sancionTipoRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTO() {
		List<SancionTipo> listaModel = List.of(new SancionTipo());
		List<SancionTipoDTO> listaDto = new ArrayList<>(List.of(new SancionTipoDTO()));

		when(sancionTipoRepository.findByActivoTrue()).thenReturn(listaModel);
		when(sancionTipoMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<SancionTipoDTO> resultado = sancionTipoService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(sancionTipoRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		SancionTipoFilter filter = new SancionTipoFilter();
		List<SancionTipo> listaModel = List.of(new SancionTipo());
		List<SancionTipoDTO> listaDto = new ArrayList<>(List.of(new SancionTipoDTO()));

		when(sancionTipoRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(sancionTipoMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<SancionTipoDTO> resultado = sancionTipoService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(sancionTipoRepository).findAll(any(Specification.class));
	}

	@Test
	void canWriteYCanRead() {
		assertThat(sancionTipoService.canWrite(1L)).isTrue();
		assertThat(sancionTipoService.canRead(1L)).isTrue();
	}
}