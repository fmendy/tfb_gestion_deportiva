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

import com.gestion.deportiva.dto.UsuarioEmpresaDTO;
import com.gestion.deportiva.dto.filter.UsuarioEmpresaFilter;
import com.gestion.deportiva.mapper.UsuarioEmpresaMapper;
import com.gestion.deportiva.model.UsuarioEmpresa;
import com.gestion.deportiva.repository.UsuarioEmpresaRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioEmpresaServiceImplTest {

	@Mock
	private UsuarioEmpresaRepository usuarioEmpresaRepository;

	@Mock
	private UsuarioEmpresaMapper usuarioEmpresaMapper;

	@InjectMocks
	private UsuarioEmpresaServiceImpl usuarioEmpresaService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setId(id);
		UsuarioEmpresaDTO dto = new UsuarioEmpresaDTO();

		when(usuarioEmpresaRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(usuarioEmpresaMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioEmpresaDTO resultado = usuarioEmpresaService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioEmpresaRepository).findByActivoTrueAndId(id);
		verify(usuarioEmpresaMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		UsuarioEmpresa model = new UsuarioEmpresa();
		UsuarioEmpresaDTO dto = new UsuarioEmpresaDTO();

		when(usuarioEmpresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(usuarioEmpresaMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioEmpresaDTO resultado = usuarioEmpresaService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioEmpresaRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(usuarioEmpresaMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		UsuarioEmpresaDTO dto = new UsuarioEmpresaDTO();
		dto.setUuid("uuid-nuevo");

		when(usuarioEmpresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(usuarioEmpresaMapper.dtoToModel(any(UsuarioEmpresaDTO.class), any(UsuarioEmpresa.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		usuarioEmpresaService.guardar(dto);

		verify(usuarioEmpresaRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(usuarioEmpresaRepository).saveAndFlush(any(UsuarioEmpresa.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		UsuarioEmpresaDTO dto = new UsuarioEmpresaDTO();
		dto.setUuid("uuid-existente");

		UsuarioEmpresa modelExistente = new UsuarioEmpresa();
		modelExistente.setId(10L);

		when(usuarioEmpresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(usuarioEmpresaMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = usuarioEmpresaService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(usuarioEmpresaRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		UsuarioEmpresaFilter filter = new UsuarioEmpresaFilter();
		Pageable pageable = PageRequest.of(0, 10);
		UsuarioEmpresa model = new UsuarioEmpresa();
		Page<UsuarioEmpresa> pageModel = new PageImpl<>(List.of(model));
		Page<UsuarioEmpresaDTO> pageDto = new PageImpl<>(List.of(new UsuarioEmpresaDTO()));

		when(usuarioEmpresaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(usuarioEmpresaMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<UsuarioEmpresaDTO> resultado = usuarioEmpresaService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(usuarioEmpresaRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setActivo(true);

		when(usuarioEmpresaRepository.findByActivoTrueAndId(id)).thenReturn(model);

		usuarioEmpresaService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(usuarioEmpresaRepository).saveAndFlush(model);
	}

	@Test
	void eliminarByUsuarioIdConRegistros() {
		Long usuarioId = 5L;
		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setActivo(true);

		when(usuarioEmpresaRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of(model));

		usuarioEmpresaService.eliminarByUsuarioId(usuarioId);

		assertThat(model.isActivo()).isFalse();
		verify(usuarioEmpresaRepository).saveAll(any());
	}

	@Test
	void eliminarByUsuarioIdSinRegistros() {
		Long usuarioId = 5L;

		when(usuarioEmpresaRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of());

		usuarioEmpresaService.eliminarByUsuarioId(usuarioId);

		verify(usuarioEmpresaRepository).findByActivoTrueAndUsuarioId(usuarioId);
	}

	@Test
	void obtenerListDTO() {
		List<UsuarioEmpresa> listaModel = List.of(new UsuarioEmpresa());
		List<UsuarioEmpresaDTO> listaDto = List.of(new UsuarioEmpresaDTO());

		when(usuarioEmpresaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(usuarioEmpresaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioEmpresaDTO> resultado = usuarioEmpresaService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(usuarioEmpresaRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		UsuarioEmpresaFilter filter = new UsuarioEmpresaFilter();
		List<UsuarioEmpresa> listaModel = List.of(new UsuarioEmpresa());
		List<UsuarioEmpresaDTO> listaDto = List.of(new UsuarioEmpresaDTO());

		when(usuarioEmpresaRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(usuarioEmpresaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioEmpresaDTO> resultado = usuarioEmpresaService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(usuarioEmpresaRepository).findAll(any(Specification.class));
	}

	@Test
	void asociarUsuarioEmpresa() {
		Long usuarioId = 1L;
		Long empresaId = 2L;

		when(usuarioEmpresaRepository.saveAndFlush(any(UsuarioEmpresa.class))).thenAnswer(invocation -> {
			UsuarioEmpresa ue = invocation.getArgument(0);
			ue.setId(100L);
			return ue;
		});

		Long resultadoId = usuarioEmpresaService.asociarUsuarioEmpresa(usuarioId, empresaId);

		assertThat(resultadoId).isEqualTo(100L);
		verify(usuarioEmpresaRepository).saveAndFlush(any(UsuarioEmpresa.class));
	}

	@Test
	void getListByUsuarioId() {
		Long usuarioId = 1L;
		List<UsuarioEmpresa> lista = List.of(new UsuarioEmpresa());

		when(usuarioEmpresaRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(lista);

		List<UsuarioEmpresa> resultado = usuarioEmpresaService.getListByUsuarioId(usuarioId);

		assertThat(resultado).isEqualTo(lista);
		verify(usuarioEmpresaRepository).findByActivoTrueAndUsuarioId(usuarioId);
	}

	@Test
	void canWriteYCanRead() {
		assertThat(usuarioEmpresaService.canWrite(1L)).isTrue();
		assertThat(usuarioEmpresaService.canRead(1L)).isTrue();
	}
}