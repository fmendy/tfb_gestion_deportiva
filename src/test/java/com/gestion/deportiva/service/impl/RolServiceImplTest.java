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
import com.gestion.deportiva.dto.RolDTO;
import com.gestion.deportiva.dto.filter.RolFilter;
import com.gestion.deportiva.mapper.RolMapper;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.repository.RolRepository;

@ExtendWith(MockitoExtension.class)
class RolServiceImplTest {

	@Mock
	private RolRepository rolRepository;

	@Mock
	private RolMapper rolMapper;

	@InjectMocks
	private RolServiceImpl rolService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		Rol model = new Rol();
		model.setId(id);
		RolDTO dto = new RolDTO();

		when(rolRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(rolMapper.modelToDTO(model)).thenReturn(dto);

		RolDTO resultado = rolService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(rolRepository).findByActivoTrueAndId(id);
		verify(rolMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		Rol model = new Rol();
		RolDTO dto = new RolDTO();

		when(rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(rolMapper.modelToDTO(model)).thenReturn(dto);

		RolDTO resultado = rolService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(rolRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(rolMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		RolDTO dto = new RolDTO();
		dto.setUuid("uuid-nuevo");

		when(rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(rolMapper.dtoToModel(any(RolDTO.class), any(Rol.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		Long id = rolService.guardar(dto);

		verify(rolRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(rolRepository).saveAndFlush(any(Rol.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		RolDTO dto = new RolDTO();
		dto.setUuid("uuid-existente");

		Rol modelExistente = new Rol();
		modelExistente.setId(10L);

		when(rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente")).thenReturn(modelExistente);
		when(rolMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = rolService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(rolRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		RolFilter filter = new RolFilter();
		Pageable pageable = PageRequest.of(0, 10);
		Rol model = new Rol();
		Page<Rol> pageModel = new PageImpl<>(List.of(model));
		Page<RolDTO> pageDto = new PageImpl<>(List.of(new RolDTO()));

		when(rolRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(rolMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<RolDTO> resultado = rolService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(rolRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		Rol model = new Rol();
		model.setActivo(true);

		when(rolRepository.findByActivoTrueAndId(id)).thenReturn(model);

		rolService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(rolRepository).saveAndFlush(model);
	}

	@Test
	void eliminarPorUuid() {
		String uuid = "uuid-del";
		Rol model = new Rol();
		model.setActivo(true);

		when(rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);

		rolService.eliminar(uuid);

		assertThat(model.isActivo()).isFalse();
		verify(rolRepository).saveAndFlush(model);
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Administrador";
		Rol model = new Rol();
		RolDTO dto = new RolDTO();

		when(rolRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(model);
		when(rolMapper.modelToDTO(model)).thenReturn(dto);

		RolDTO resultado = rolService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(rolRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
		verify(rolMapper).modelToDTO(model);
	}

	@Test
	void obtenerListComboDTO() {
		List<Rol> listaModel = List.of(new Rol());
		List<ComboDTO> listaComboDto = List.of(new ComboDTO());

		when(rolRepository.findByActivoTrueOrderByNombreAsc()).thenReturn(listaModel);
		when(rolMapper.listModelToListComboDTO(listaModel)).thenReturn(listaComboDto);

		List<ComboDTO> resultado = rolService.getListComboDTO();

		assertThat(resultado).isNotNull();
		verify(rolRepository).findByActivoTrueOrderByNombreAsc();
	}

	@Test
	void obtenerListDTO() {
		List<Rol> listaModel = List.of(new Rol());
		List<RolDTO> listaDto = List.of(new RolDTO());

		when(rolRepository.findByActivoTrue()).thenReturn(listaModel);
		when(rolMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		verify(rolRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		RolFilter filter = new RolFilter();
		List<Rol> listaModel = List.of(new Rol());
		List<RolDTO> listaDto = List.of(new RolDTO());

		when(rolRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(rolMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);


		verify(rolRepository).findAll(any(Specification.class));
	}

	@Test
	void getFilterParaUsuarioController() {
		RolFilter filter = rolService.getFilterParaUsuarioController();
		assertThat(filter).isNotNull();
	}

	@Test
	void canWriteYCanRead() {
		assertThat(rolService.canWrite(1L)).isTrue();
		assertThat(rolService.canRead(1L)).isTrue();
	}
}