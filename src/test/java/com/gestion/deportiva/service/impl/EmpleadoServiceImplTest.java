package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.EmpleadoDTO;
import com.gestion.deportiva.dto.filter.EmpleadoFilter;
import com.gestion.deportiva.mapper.EmpleadoMapper;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceImplTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private EmpleadoMapper empleadoMapper;

	@InjectMocks
	private EmpleadoServiceImpl empleadoService;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		securityUtilMockedStatic.close();
	}

	@Test
	void buscarPorId() {
		Long id = 1L;
		Usuario usuario = new Usuario();
		usuario.setId(id);
		EmpleadoDTO dto = new EmpleadoDTO();

		when(usuarioRepository.findByActivoTrueAndId(id)).thenReturn(usuario);
		when(empleadoMapper.modelToDTO(usuario)).thenReturn(dto);

		EmpleadoDTO resultado = empleadoService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioRepository).findByActivoTrueAndId(id);
		verify(empleadoMapper).modelToDTO(usuario);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		Usuario usuario = new Usuario();
		EmpleadoDTO dto = new EmpleadoDTO();

		when(usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(usuario);
		when(empleadoMapper.modelToDTO(usuario)).thenReturn(dto);

		EmpleadoDTO resultado = empleadoService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(empleadoMapper).modelToDTO(usuario);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		EmpleadoFilter filter = new EmpleadoFilter();
		Pageable pageable = PageRequest.of(0, 10);
		Usuario usuario = new Usuario();
		Page<Usuario> pageUsuario = new PageImpl<>(List.of(usuario));
		Page<EmpleadoDTO> pageDto = new PageImpl<>(List.of(new EmpleadoDTO()));

		securityUtilMockedStatic.when(SecurityUtil::hasGlobalAccess).thenReturn(true);

		when(usuarioRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageUsuario);
		when(empleadoMapper.pageToPageDTO(pageUsuario)).thenReturn(pageDto);

		Page<EmpleadoDTO> resultado = empleadoService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(usuarioRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Juan";
		Usuario usuario = new Usuario();
		EmpleadoDTO dto = new EmpleadoDTO();

		when(usuarioRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(usuario);
		when(empleadoMapper.modelToDTO(usuario)).thenReturn(dto);

		EmpleadoDTO resultado = empleadoService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
	}
}