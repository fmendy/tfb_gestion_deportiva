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
import com.gestion.deportiva.dto.InstalacionTipoDTO;
import com.gestion.deportiva.dto.filter.InstalacionTipoFilter;
import com.gestion.deportiva.mapper.InstalacionTipoMapper;
import com.gestion.deportiva.model.InstalacionTipo;
import com.gestion.deportiva.repository.InstalacionTipoRepository;

@ExtendWith(MockitoExtension.class)
class InstalacionTipoServiceImplTest {

	@Mock
	private InstalacionTipoRepository instalacionTipoRepository;

	@Mock
	private InstalacionTipoMapper instalacionTipoMapper;

	@InjectMocks
	private InstalacionTipoServiceImpl instalacionTipoService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		InstalacionTipo model = new InstalacionTipo();
		model.setId(id);
		InstalacionTipoDTO dto = new InstalacionTipoDTO();

		when(instalacionTipoRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(instalacionTipoMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionTipoDTO resultado = instalacionTipoService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionTipoRepository).findByActivoTrueAndId(id);
		verify(instalacionTipoMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		InstalacionTipo model = new InstalacionTipo();
		InstalacionTipoDTO dto = new InstalacionTipoDTO();

		when(instalacionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(instalacionTipoMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionTipoDTO resultado = instalacionTipoService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionTipoRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(instalacionTipoMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		InstalacionTipoDTO dto = new InstalacionTipoDTO();
		dto.setUuid("uuid-nuevo");

		when(instalacionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(instalacionTipoMapper.dtoToModel(any(InstalacionTipoDTO.class), any(InstalacionTipo.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		instalacionTipoService.guardar(dto);

		verify(instalacionTipoRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(instalacionTipoRepository).saveAndFlush(any(InstalacionTipo.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		InstalacionTipoDTO dto = new InstalacionTipoDTO();
		dto.setUuid("uuid-existente");

		InstalacionTipo modelExistente = new InstalacionTipo();
		modelExistente.setId(10L);

		when(instalacionTipoRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(instalacionTipoMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = instalacionTipoService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(instalacionTipoRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		InstalacionTipoFilter filter = new InstalacionTipoFilter();
		Pageable pageable = PageRequest.of(0, 10);
		InstalacionTipo model = new InstalacionTipo();
		Page<InstalacionTipo> pageModel = new PageImpl<>(List.of(model));
		Page<InstalacionTipoDTO> pageDto = new PageImpl<>(List.of(new InstalacionTipoDTO()));

		when(instalacionTipoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(instalacionTipoMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<InstalacionTipoDTO> resultado = instalacionTipoService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(instalacionTipoRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		InstalacionTipo model = new InstalacionTipo();
		model.setActivo(true);

		when(instalacionTipoRepository.findByActivoTrueAndId(id)).thenReturn(model);

		instalacionTipoService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(instalacionTipoRepository).saveAndFlush(model);
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Padel";
		InstalacionTipo model = new InstalacionTipo();
		InstalacionTipoDTO dto = new InstalacionTipoDTO();

		when(instalacionTipoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(model);
		when(instalacionTipoMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionTipoDTO resultado = instalacionTipoService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(instalacionTipoRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
		verify(instalacionTipoMapper).modelToDTO(model);
	}

	@Test
	void obtenerListComboDTO() {
		List<InstalacionTipo> listaModel = List.of(new InstalacionTipo());
		List<ComboDTO> listaComboDto = List.of(new ComboDTO());

		when(instalacionTipoRepository.findByActivoTrue()).thenReturn(listaModel);
		when(instalacionTipoMapper.listModelToListComboDTO(listaModel)).thenReturn(listaComboDto);

		List<ComboDTO> resultado = instalacionTipoService.getListComboDTO();

		assertThat(resultado).isNotNull();
		verify(instalacionTipoRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTO() {
		List<InstalacionTipo> listaModel = List.of(new InstalacionTipo());
		List<InstalacionTipoDTO> listaDto = new ArrayList(List.of(new InstalacionTipoDTO()));

		when(instalacionTipoRepository.findByActivoTrue()).thenReturn(listaModel);
		when(instalacionTipoMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionTipoDTO> resultado = instalacionTipoService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(instalacionTipoRepository).findByActivoTrue();
		verify(instalacionTipoMapper).listModelToListDTO(listaModel);
	}

	@Test
	void obtenerListDTOConFiltro() {
		InstalacionTipoFilter filter = new InstalacionTipoFilter();
		List<InstalacionTipo> listaModel = List.of(new InstalacionTipo());
		List<InstalacionTipoDTO> listaDto = new ArrayList<>(List.of(new InstalacionTipoDTO()));

		when(instalacionTipoRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(instalacionTipoMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<InstalacionTipoDTO> resultado = instalacionTipoService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(instalacionTipoRepository).findAll(any(Specification.class));
	}

	@Test
	void canWriteYCanRead() {
		assertThat(instalacionTipoService.canWrite(1L)).isTrue();
		assertThat(instalacionTipoService.canRead(1L)).isTrue();
	}
}