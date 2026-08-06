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

import com.gestion.deportiva.dto.UsuarioInstalacionDTO;
import com.gestion.deportiva.dto.filter.UsuarioInstalacionFilter;
import com.gestion.deportiva.mapper.UsuarioInstalacionMapper;
import com.gestion.deportiva.model.UsuarioInstalacion;
import com.gestion.deportiva.repository.UsuarioInstalacionRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioInstalacionServiceImplTest {

	@Mock
	private UsuarioInstalacionRepository usuarioInstalacionRepository;

	@Mock
	private UsuarioInstalacionMapper usuarioInstalacionMapper;

	@InjectMocks
	private UsuarioInstalacionServiceImpl usuarioInstalacionService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		UsuarioInstalacion model = new UsuarioInstalacion();
		model.setId(id);
		UsuarioInstalacionDTO dto = new UsuarioInstalacionDTO();

		when(usuarioInstalacionRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(usuarioInstalacionMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioInstalacionDTO resultado = usuarioInstalacionService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioInstalacionRepository).findByActivoTrueAndId(id);
		verify(usuarioInstalacionMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		UsuarioInstalacion model = new UsuarioInstalacion();
		UsuarioInstalacionDTO dto = new UsuarioInstalacionDTO();

		when(usuarioInstalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(usuarioInstalacionMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioInstalacionDTO resultado = usuarioInstalacionService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioInstalacionRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(usuarioInstalacionMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		UsuarioInstalacionDTO dto = new UsuarioInstalacionDTO();
		dto.setUuid("uuid-nuevo");

		when(usuarioInstalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(usuarioInstalacionMapper.dtoToModel(any(UsuarioInstalacionDTO.class), any(UsuarioInstalacion.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		Long id = usuarioInstalacionService.guardar(dto);

		verify(usuarioInstalacionRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(usuarioInstalacionRepository).saveAndFlush(any(UsuarioInstalacion.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		UsuarioInstalacionDTO dto = new UsuarioInstalacionDTO();
		dto.setUuid("uuid-existente");

		UsuarioInstalacion modelExistente = new UsuarioInstalacion();
		modelExistente.setId(10L);

		when(usuarioInstalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(usuarioInstalacionMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = usuarioInstalacionService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(usuarioInstalacionRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		UsuarioInstalacionFilter filter = new UsuarioInstalacionFilter();
		Pageable pageable = PageRequest.of(0, 10);
		UsuarioInstalacion model = new UsuarioInstalacion();
		Page<UsuarioInstalacion> pageModel = new PageImpl<>(List.of(model));
		Page<UsuarioInstalacionDTO> pageDto = new PageImpl<>(List.of(new UsuarioInstalacionDTO()));

		when(usuarioInstalacionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(usuarioInstalacionMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<UsuarioInstalacionDTO> resultado = usuarioInstalacionService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(usuarioInstalacionRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		UsuarioInstalacion model = new UsuarioInstalacion();
		model.setActivo(true);

		when(usuarioInstalacionRepository.findByActivoTrueAndId(id)).thenReturn(model);

		usuarioInstalacionService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(usuarioInstalacionRepository).saveAndFlush(model);
	}

	@Test
	void obtenerListDTO() {
		List<UsuarioInstalacion> listaModel = List.of(new UsuarioInstalacion());
		List<UsuarioInstalacionDTO> listaDto = List.of(new UsuarioInstalacionDTO());

		when(usuarioInstalacionRepository.findByActivoTrue()).thenReturn(listaModel);
		when(usuarioInstalacionMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioInstalacionDTO> resultado = usuarioInstalacionService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(usuarioInstalacionRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		UsuarioInstalacionFilter filter = new UsuarioInstalacionFilter();
		List<UsuarioInstalacion> listaModel = List.of(new UsuarioInstalacion());
		List<UsuarioInstalacionDTO> listaDto = List.of(new UsuarioInstalacionDTO());

		when(usuarioInstalacionRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(usuarioInstalacionMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioInstalacionDTO> resultado = usuarioInstalacionService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(usuarioInstalacionRepository).findAll(any(Specification.class));
	}

	@Test
	void asociarUsuarioInstalacion() {
		Long usuarioId = 1L;
		Long instalacionId = 2L;

		when(usuarioInstalacionRepository.saveAndFlush(any(UsuarioInstalacion.class))).thenAnswer(invocation -> {
			UsuarioInstalacion ui = invocation.getArgument(0);
			ui.setId(100L);
			return ui;
		});

		Long resultadoId = usuarioInstalacionService.asociarUsuarioInstalacion(usuarioId, instalacionId);

		assertThat(resultadoId).isEqualTo(100L);
		verify(usuarioInstalacionRepository).saveAndFlush(any(UsuarioInstalacion.class));
	}

	@Test
	void getListByUsuarioId() {
		Long usuarioId = 1L;
		List<UsuarioInstalacion> lista = List.of(new UsuarioInstalacion());

		when(usuarioInstalacionRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(lista);

		List<UsuarioInstalacion> resultado = usuarioInstalacionService.getListByUsuarioId(usuarioId);

		assertThat(resultado).isEqualTo(lista);
		verify(usuarioInstalacionRepository).findByActivoTrueAndUsuarioId(usuarioId);
	}

	@Test
	void eliminarByUsuarioIdConRegistros() {
		Long usuarioId = 5L;
		UsuarioInstalacion model = new UsuarioInstalacion();
		model.setActivo(true);

		when(usuarioInstalacionRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of(model));

		usuarioInstalacionService.eliminarByUsuarioId(usuarioId);

		assertThat(model.isActivo()).isFalse();
		verify(usuarioInstalacionRepository).saveAll(any());
	}

	@Test
	void eliminarByUsuarioIdSinRegistros() {
		Long usuarioId = 5L;

		when(usuarioInstalacionRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of());

		usuarioInstalacionService.eliminarByUsuarioId(usuarioId);

		verify(usuarioInstalacionRepository).findByActivoTrueAndUsuarioId(usuarioId);
	}

	@Test
	void canWriteYCanRead() {
		assertThat(usuarioInstalacionService.canWrite(1L)).isTrue();
		assertThat(usuarioInstalacionService.canRead(1L)).isTrue();
	}
}