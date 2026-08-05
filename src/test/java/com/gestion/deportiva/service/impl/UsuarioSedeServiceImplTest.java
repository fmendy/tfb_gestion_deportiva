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

import com.gestion.deportiva.dto.UsuarioSedeDTO;
import com.gestion.deportiva.dto.filter.UsuarioSedeFilter;
import com.gestion.deportiva.mapper.UsuarioSedeMapper;
import com.gestion.deportiva.model.UsuarioSede;
import com.gestion.deportiva.repository.UsuarioSedeRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioSedeServiceImplTest {

	@Mock
	private UsuarioSedeRepository usuarioSedeRepository;

	@Mock
	private UsuarioSedeMapper usuarioSedeMapper;

	@InjectMocks
	private UsuarioSedeServiceImpl usuarioSedeService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		UsuarioSede model = new UsuarioSede();
		model.setId(id);
		UsuarioSedeDTO dto = new UsuarioSedeDTO();

		when(usuarioSedeRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(usuarioSedeMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioSedeDTO resultado = usuarioSedeService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioSedeRepository).findByActivoTrueAndId(id);
		verify(usuarioSedeMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		UsuarioSede model = new UsuarioSede();
		UsuarioSedeDTO dto = new UsuarioSedeDTO();

		when(usuarioSedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(usuarioSedeMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioSedeDTO resultado = usuarioSedeService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioSedeRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(usuarioSedeMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		UsuarioSedeDTO dto = new UsuarioSedeDTO();
		dto.setUuid("uuid-nuevo");

		when(usuarioSedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(usuarioSedeMapper.dtoToModel(any(UsuarioSedeDTO.class), any(UsuarioSede.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		Long id = usuarioSedeService.guardar(dto);

		verify(usuarioSedeRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(usuarioSedeRepository).saveAndFlush(any(UsuarioSede.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		UsuarioSedeDTO dto = new UsuarioSedeDTO();
		dto.setUuid("uuid-existente");

		UsuarioSede modelExistente = new UsuarioSede();
		modelExistente.setId(10L);

		when(usuarioSedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(usuarioSedeMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = usuarioSedeService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(usuarioSedeRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		UsuarioSedeFilter filter = new UsuarioSedeFilter();
		Pageable pageable = PageRequest.of(0, 10);
		UsuarioSede model = new UsuarioSede();
		Page<UsuarioSede> pageModel = new PageImpl<>(List.of(model));
		Page<UsuarioSedeDTO> pageDto = new PageImpl<>(List.of(new UsuarioSedeDTO()));

		when(usuarioSedeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(usuarioSedeMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<UsuarioSedeDTO> resultado = usuarioSedeService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(usuarioSedeRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		UsuarioSede model = new UsuarioSede();
		model.setActivo(true);

		when(usuarioSedeRepository.findByActivoTrueAndId(id)).thenReturn(model);

		usuarioSedeService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(usuarioSedeRepository).saveAndFlush(model);
	}

	@Test
	void eliminarPorUuid() {
		String uuid = "uuid-del";
		UsuarioSede model = new UsuarioSede();
		model.setActivo(true);

		when(usuarioSedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);

		usuarioSedeService.eliminar(uuid);

		assertThat(model.isActivo()).isFalse();
		verify(usuarioSedeRepository).saveAndFlush(model);
	}

	@Test
	void obtenerListDTO() {
		List<UsuarioSede> listaModel = List.of(new UsuarioSede());
		List<UsuarioSedeDTO> listaDto = List.of(new UsuarioSedeDTO());

		when(usuarioSedeRepository.findByActivoTrue()).thenReturn(listaModel);
		when(usuarioSedeMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioSedeDTO> resultado = usuarioSedeService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(usuarioSedeRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		UsuarioSedeFilter filter = new UsuarioSedeFilter();
		List<UsuarioSede> listaModel = List.of(new UsuarioSede());
		List<UsuarioSedeDTO> listaDto = List.of(new UsuarioSedeDTO());

		when(usuarioSedeRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(usuarioSedeMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioSedeDTO> resultado = usuarioSedeService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(usuarioSedeRepository).findAll(any(Specification.class));
	}

	@Test
	void asociarUsuarioSede() {
		Long usuarioId = 1L;
		Long sedeId = 2L;

		when(usuarioSedeRepository.saveAndFlush(any(UsuarioSede.class))).thenAnswer(invocation -> {
			UsuarioSede us = invocation.getArgument(0);
			us.setId(100L);
			return us;
		});

		Long resultadoId = usuarioSedeService.asociarUsuarioSede(usuarioId, sedeId);

		assertThat(resultadoId).isEqualTo(100L);
		verify(usuarioSedeRepository).saveAndFlush(any(UsuarioSede.class));
	}

	@Test
	void getListByUsuarioId() {
		Long usuarioId = 1L;
		List<UsuarioSede> lista = List.of(new UsuarioSede());

		when(usuarioSedeRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(lista);

		List<UsuarioSede> resultado = usuarioSedeService.getListByUsuarioId(usuarioId);

		assertThat(resultado).isEqualTo(lista);
		verify(usuarioSedeRepository).findByActivoTrueAndUsuarioId(usuarioId);
	}

	@Test
	void eliminarByUsuarioIdConRegistros() {
		Long usuarioId = 5L;
		UsuarioSede model = new UsuarioSede();
		model.setActivo(true);

		when(usuarioSedeRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of(model));

		usuarioSedeService.eliminarByUsuarioId(usuarioId);

		assertThat(model.isActivo()).isFalse();
		verify(usuarioSedeRepository).saveAll(any());
	}

	@Test
	void eliminarByUsuarioIdSinRegistros() {
		Long usuarioId = 5L;

		when(usuarioSedeRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of());

		usuarioSedeService.eliminarByUsuarioId(usuarioId);

		verify(usuarioSedeRepository).findByActivoTrueAndUsuarioId(usuarioId);
	}

	@Test
	void canWriteYCanRead() {
		assertThat(usuarioSedeService.canWrite(1L)).isTrue();
		assertThat(usuarioSedeService.canRead(1L)).isTrue();
	}
}