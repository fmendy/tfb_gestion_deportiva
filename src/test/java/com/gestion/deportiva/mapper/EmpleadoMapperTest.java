package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gestion.deportiva.dto.EmpleadoDTO;
import com.gestion.deportiva.dto.EmpleadoRegistroDTO;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioRol;
import com.gestion.deportiva.util.Constantes;

class EmpleadoMapperTest {

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UsuarioRolMapper usuarioRolMapper;

	@Mock
	private UsuarioEmpresaMapper usuarioEmpresaMapper;

	@Mock
	private UsuarioSedeMapper usuarioSedeMapper;

	@Mock
	private UsuarioInstalacionMapper usuarioInstalacionMapper;

	@InjectMocks
	private EmpleadoMapper empleadoMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void modelToDTOConRolNoValidoDevuelveDtoVacio() {
		Usuario usuario = new Usuario();
		usuario.setId(2L);
		usuario.setEmail("usuario@example.com");

		Rol rol = new Rol();
		rol.setNombre("CLIENTE");

		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setRol(rol);
		usuario.setListUsuarioRol(List.of(usuarioRol));

		EmpleadoDTO dto = empleadoMapper.modelToDTO(usuario);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isNull();
		assertThat(dto.getEmail()).isNull();
	}

	@Test
	void modelToEmpleadoRegistroDTOConRolValido() {
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setEmail("reg@example.com");
		usuario.setNombre("Reg Test");

		Rol rol = new Rol();
		rol.setNombre(Constantes.Rol.USUARIO_EMPRESA);

		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setRol(rol);
		usuario.setListUsuarioRol(List.of(usuarioRol));

		EmpleadoRegistroDTO dto = empleadoMapper.modelToEmpleadoRegistroDTO(usuario);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(3L);
		assertThat(dto.getEmail()).isEqualTo("reg@example.com");
		assertThat(dto.getNombre()).isEqualTo("Reg Test");
	}

	@Test
	void modelToDTOConRolEmpleadoValido() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setEmail("empleado@example.com");
		usuario.setNombre("Empleado Test");
		usuario.setListUsuarioEmpresa(List.of());
		usuario.setListUsuarioSede(List.of());
		usuario.setListUsuarioInstalacion(List.of());

		Rol rol = new Rol();
		rol.setNombre(Constantes.Rol.ADMINISTRADOR);

		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setRol(rol);
		usuario.setListUsuarioRol(List.of(usuarioRol));

		when(usuarioRolMapper.listModelToListDTO(anyList())).thenReturn(List.of());
		when(usuarioEmpresaMapper.listModelToListDTO(anyList())).thenReturn(List.of());
		when(usuarioSedeMapper.listModelToListDTO(anyList())).thenReturn(List.of());
		when(usuarioInstalacionMapper.listModelToListDTO(anyList())).thenReturn(List.of());

		EmpleadoDTO dto = empleadoMapper.modelToDTO(usuario);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getEmail()).isEqualTo("empleado@example.com");
		assertThat(dto.getNombre()).isEqualTo("Empleado Test");
	}

	@Test
	void listModelToListDTO() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setEmail("test@example.com");
		usuario.setListUsuarioEmpresa(List.of());
		usuario.setListUsuarioSede(List.of());
		usuario.setListUsuarioInstalacion(List.of());

		Rol rol = new Rol();
		rol.setNombre(Constantes.Rol.ADMINISTRADOR);
		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setRol(rol);
		usuario.setListUsuarioRol(List.of(usuarioRol));

		when(usuarioRolMapper.listModelToListDTO(anyList())).thenReturn(List.of());
		when(usuarioEmpresaMapper.listModelToListDTO(anyList())).thenReturn(List.of());
		when(usuarioSedeMapper.listModelToListDTO(anyList())).thenReturn(List.of());
		when(usuarioInstalacionMapper.listModelToListDTO(anyList())).thenReturn(List.of());

		List<EmpleadoDTO> dtos = empleadoMapper.listModelToListDTO(List.of(usuario));

		assertThat(dtos).hasSize(1);
	}

	@Test
	void pageToPageDTO() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setEmail("page@example.com");
		usuario.setListUsuarioEmpresa(List.of());
		usuario.setListUsuarioSede(List.of());
		usuario.setListUsuarioInstalacion(List.of());

		Rol rol = new Rol();
		rol.setNombre(Constantes.Rol.ADMINISTRADOR);
		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setRol(rol);
		usuario.setListUsuarioRol(List.of(usuarioRol));

		when(usuarioRolMapper.listModelToListDTO(anyList())).thenReturn(List.of());
		when(usuarioEmpresaMapper.listModelToListDTO(anyList())).thenReturn(List.of());
		when(usuarioSedeMapper.listModelToListDTO(anyList())).thenReturn(List.of());
		when(usuarioInstalacionMapper.listModelToListDTO(anyList())).thenReturn(List.of());

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Usuario> pageModel = new PageImpl<>(List.of(usuario), pageable, 1);

		Page<EmpleadoDTO> pageDto = empleadoMapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}
}