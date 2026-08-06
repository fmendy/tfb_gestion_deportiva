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

import com.gestion.deportiva.dto.UsuarioRolDTO;
import com.gestion.deportiva.dto.filter.UsuarioRolFilter;
import com.gestion.deportiva.mapper.UsuarioRolMapper;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioRol;
import com.gestion.deportiva.repository.RolRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.repository.UsuarioRolRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioRolServiceImplTest {

	@Mock
	private UsuarioRolRepository usuarioRolRepository;

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private RolRepository rolRepository;

	@Mock
	private UsuarioRolMapper usuarioRolMapper;

	@InjectMocks
	private UsuarioRolServiceImpl usuarioRolService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		UsuarioRol model = new UsuarioRol();
		model.setId(id);
		UsuarioRolDTO dto = new UsuarioRolDTO();

		when(usuarioRolRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(usuarioRolMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioRolDTO resultado = usuarioRolService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioRolRepository).findByActivoTrueAndId(id);
		verify(usuarioRolMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		UsuarioRol model = new UsuarioRol();
		UsuarioRolDTO dto = new UsuarioRolDTO();

		when(usuarioRolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(usuarioRolMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioRolDTO resultado = usuarioRolService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioRolRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(usuarioRolMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		UsuarioRolDTO dto = new UsuarioRolDTO();
		dto.setUuid("uuid-nuevo");
		dto.setRolUuid("rol-uuid");
		dto.setUsuarioUuid("user-uuid");
		dto.setId(10L);

		Rol rol = new Rol();
		rol.setId(1L);
		Usuario usuario = new Usuario();
		usuario.setId(2L);
		UsuarioRol model = new UsuarioRol();

		when(usuarioRolRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase("rol-uuid")).thenReturn(rol);
		when(usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase("user-uuid")).thenReturn(usuario);
		when(usuarioRolMapper.dtoToModel(dto, model)).thenReturn(model);

		Long id = usuarioRolService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(usuarioRolRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(rolRepository).findByActivoTrueAndUuidEqualsIgnoreCase("rol-uuid");
		verify(usuarioRepository).findByActivoTrueAndUuidEqualsIgnoreCase("user-uuid");
	}

	@Test
	void obtenerPaginaPorFiltro() {
		UsuarioRolFilter filter = new UsuarioRolFilter();
		Pageable pageable = PageRequest.of(0, 10);
		UsuarioRol model = new UsuarioRol();
		Page<UsuarioRol> pageModel = new PageImpl<>(List.of(model));
		Page<UsuarioRolDTO> pageDto = new PageImpl<>(List.of(new UsuarioRolDTO()));

		when(usuarioRolRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(usuarioRolMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<UsuarioRolDTO> resultado = usuarioRolService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(usuarioRolRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorIdEncontrado() {
		Long id = 1L;
		UsuarioRol model = new UsuarioRol();
		model.setActivo(true);

		when(usuarioRolRepository.findByActivoTrueAndId(id)).thenReturn(model);

		usuarioRolService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(usuarioRolRepository).saveAndFlush(model);
	}

	@Test
	void eliminarPorIdNoEncontrado() {
		Long id = 1L;

		when(usuarioRolRepository.findByActivoTrueAndId(id)).thenReturn(null);

		usuarioRolService.eliminar(id);

		verify(usuarioRolRepository).findByActivoTrueAndId(id);
	}

	@Test
	void obtenerListDTO() {
		List<UsuarioRol> listaModel = List.of(new UsuarioRol());
		List<UsuarioRolDTO> listaDto = List.of(new UsuarioRolDTO());

		when(usuarioRolRepository.findByActivoTrue()).thenReturn(listaModel);
		when(usuarioRolMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioRolDTO> resultado = usuarioRolService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(usuarioRolRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		UsuarioRolFilter filter = new UsuarioRolFilter();
		List<UsuarioRol> listaModel = List.of(new UsuarioRol());
		List<UsuarioRolDTO> listaDto = List.of(new UsuarioRolDTO());

		when(usuarioRolRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(usuarioRolMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioRolDTO> resultado = usuarioRolService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(usuarioRolRepository).findAll(any(Specification.class));
	}

	@Test
	void asignarRolPorNombre() {
		Long usuarioId = 1L;
		String rolNombre = "ADMIN";
		Rol rol = new Rol();

		when(usuarioRolRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of());
		when(rolRepository.findByActivoTrueAndNombreEqualsIgnoreCase(rolNombre)).thenReturn(rol);
		when(usuarioRolRepository.saveAndFlush(any(UsuarioRol.class))).thenReturn(new UsuarioRol());

		usuarioRolService.asignarRol(usuarioId, rolNombre);

		verify(usuarioRolRepository).findByActivoTrueAndUsuarioId(usuarioId);
		verify(rolRepository).findByActivoTrueAndNombreEqualsIgnoreCase(rolNombre);
		verify(usuarioRolRepository).saveAndFlush(any(UsuarioRol.class));
	}

	@Test
	void asignarRolPorId() {
		Long usuarioId = 1L;
		Long rolId = 2L;

		when(usuarioRolRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of());
		when(usuarioRolRepository.saveAndFlush(any(UsuarioRol.class))).thenAnswer(invocation -> {
			UsuarioRol ur = invocation.getArgument(0);
			ur.setId(55L);
			return ur;
		});

		Long resultadoId = usuarioRolService.asignarRol(usuarioId, rolId);

		assertThat(resultadoId).isEqualTo(55L);
		verify(usuarioRolRepository).findByActivoTrueAndUsuarioId(usuarioId);
		verify(usuarioRolRepository).saveAndFlush(any(UsuarioRol.class));
	}

	@Test
	void eliminarRolesByUsuarioId() {
		Long usuarioId = 1L;
		UsuarioRol ur = new UsuarioRol();
		ur.setActivo(true);

		when(usuarioRolRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of(ur));

		usuarioRolService.eliminarRolesByUsuarioId(usuarioId);

		assertThat(ur.isActivo()).isFalse();
		verify(usuarioRolRepository).save(ur);
	}

	@Test
	void getUsuarioRolByUsuarioId() {
		Long usuarioId = 1L;
		UsuarioRol ur = new UsuarioRol();

		when(usuarioRolRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of(ur));

		UsuarioRol resultado = usuarioRolService.getUsuarioRolByUsuarioId(usuarioId);

		assertThat(resultado).isEqualTo(ur);
		verify(usuarioRolRepository).findByActivoTrueAndUsuarioId(usuarioId);
	}

}