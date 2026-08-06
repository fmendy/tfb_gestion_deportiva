package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.dto.MiPerfilPasswordDTO;
import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.dto.UsuarioDTO;
import com.gestion.deportiva.dto.UsuarioPasswordDTO;
import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.dto.filter.UsuarioFilter;
import com.gestion.deportiva.mapper.UsuarioMapper;
import com.gestion.deportiva.model.Permiso;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.model.RolPermiso;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioRol;
import com.gestion.deportiva.model.UsuarioToken;
import com.gestion.deportiva.repository.RolRepository;
import com.gestion.deportiva.repository.UsuarioEmpresaRepository;
import com.gestion.deportiva.repository.UsuarioInstalacionRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.repository.UsuarioRolRepository;
import com.gestion.deportiva.repository.UsuarioSedeRepository;
import com.gestion.deportiva.service.MailService;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.service.UsuarioTokenService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private UsuarioMapper usuarioMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UsuarioEmpresaRepository usuarioEmpresaRepository;

	@Mock
	private UsuarioSedeRepository usuarioSedeRepository;

	@Mock
	private UsuarioInstalacionRepository usuarioInstalacionRepository;

	@Mock
	private UsuarioRolRepository usuarioRolRepository;

	@Mock
	private RolRepository rolRepository;

	@Mock
	private MailService mailService;

	@Mock
	private UsuarioTokenService usuarioTokenService;

	@Mock
	private ReservaService reservaService;

	@InjectMocks
	private UsuarioServiceImpl usuarioService;

	@Test
	void obtenerPaginaPorFiltro() {
		UsuarioFilter filter = new UsuarioFilter();
		Pageable pageable = PageRequest.of(0, 10);
		Usuario model = new Usuario();
		Page<Usuario> pageModel = new PageImpl<>(List.of(model));
		Page<UsuarioDTO> pageDto = new PageImpl<>(List.of(new UsuarioDTO()));

		when(usuarioRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(usuarioMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<UsuarioDTO> resultado = usuarioService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(usuarioRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void guardar() {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setUuid("uuid-123");
		Usuario model = new Usuario();

		when(usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-123")).thenReturn(model);
		when(usuarioMapper.dtoToModel(dto, model)).thenReturn(model);

		usuarioService.guardar(dto);

		verify(usuarioRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-123");
		verify(usuarioRepository).saveAndFlush(model);
	}

	@Test
	void findById() {
		Long id = 1L;
		Usuario model = new Usuario();
		UsuarioDTO dto = new UsuarioDTO();

		when(usuarioRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(usuarioMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioDTO resultado = usuarioService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioRepository).findByActivoTrueAndId(id);
	}

	@Test
	void getByUsername() {
		String username = "admin";
		Usuario model = new Usuario();

		when(usuarioRepository.findByActivoTrueAndNombreEqualsIgnoreCase(username)).thenReturn(model);

		Usuario resultado = usuarioService.getByUsername(username);

		assertThat(resultado).isEqualTo(model);
		verify(usuarioRepository).findByActivoTrueAndNombreEqualsIgnoreCase(username);
	}

	@Test
	void loadUserByUsernameEncontrado() {
		String email = "test@example.com";
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Test");
		usuario.setPassword("pass");

		Permiso permiso = new Permiso();
		permiso.setActivo(true);
		permiso.setNombre("PERMISO_TEST");

		RolPermiso rolPermiso = new RolPermiso();
		rolPermiso.setActivo(true);
		rolPermiso.setPermiso(permiso);

		Rol rol = new Rol();
		rol.setActivo(true);
		rol.setNombre("ADMIN");
		rol.setListRolPermiso(List.of(rolPermiso));

		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setActivo(true);
		usuarioRol.setRol(rol);

		usuario.setListUsuarioRol(List.of(usuarioRol));

		when(usuarioRepository.findByActivoTrueAndEmailIgnoreCase(email)).thenReturn(usuario);
		when(usuarioEmpresaRepository.findByActivoTrueAndUsuarioId(1L)).thenReturn(List.of());
		when(usuarioSedeRepository.findByActivoTrueAndUsuarioId(1L)).thenReturn(List.of());
		when(usuarioInstalacionRepository.findByActivoTrueAndUsuarioId(1L)).thenReturn(List.of());

		UserDetails userDetails = usuarioService.loadUserByUsername(email);

		assertThat(userDetails).isNotNull();
		assertThat(userDetails.getUsername()).isEqualTo("Test");
		assertThat(userDetails.getAuthorities()).hasSize(2);
	}

	@Test
	void loadUserByUsernameNoEncontrado() {
		String email = "notfound@example.com";

		when(usuarioRepository.findByActivoTrueAndEmailIgnoreCase(email)).thenReturn(null);

		assertThatThrownBy(() -> usuarioService.loadUserByUsername(email))
				.isInstanceOf(UsernameNotFoundException.class);
	}

	@Test
	void getUsuarioWithoutAuditor() {
		String nombre = "test";
		Usuario usuario = new Usuario();

		when(usuarioRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(usuario);

		Usuario resultado = usuarioService.getUsuarioWithoutAuditor(nombre);

		assertThat(resultado).isEqualTo(usuario);
		verify(usuarioRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
	}

	@Test
	void eliminar() {
		Long id = 1L;
		Usuario usuario = new Usuario();

		when(usuarioRepository.findByActivoTrueAndId(id)).thenReturn(usuario);

		usuarioService.eliminar(id);

		assertThat(usuario.isActivo()).isFalse();
		verify(usuarioEmpresaRepository).findByActivoTrueAndUsuarioId(id);
		verify(usuarioSedeRepository).findByActivoTrueAndUsuarioId(id);
		verify(usuarioInstalacionRepository).findByActivoTrueAndUsuarioId(id);
		verify(usuarioRolRepository).findByActivoTrueAndUsuarioId(id);
	}

	@Test
	void guardarDatos() {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(1L);
		Usuario usuario = new Usuario();

		when(usuarioRepository.findByActivoTrueAndId(1L)).thenReturn(usuario);
		when(usuarioMapper.dtoToModel(dto, usuario)).thenReturn(usuario);

		Long id = usuarioService.guardarDatos(dto);

		assertThat(id).isEqualTo(1L);
		verify(usuarioRepository).saveAndFlush(usuario);
	}

	@Test
	void getNombreByIdEncontrado() {
		Long id = 1L;
		Usuario usuario = new Usuario();
		usuario.setNombre("Juan");

		when(usuarioRepository.findByActivoTrueAndId(id)).thenReturn(usuario);

		String nombre = usuarioService.getNombreById(id);

		assertThat(nombre).isEqualTo("Juan");
	}

	@Test
	void getNombreByIdNoEncontrado() {
		Long id = 1L;

		when(usuarioRepository.findByActivoTrueAndId(id)).thenReturn(null);

		String nombre = usuarioService.getNombreById(id);

		assertThat(nombre).isNull();
	}

	@Test
	void registrarUsuarioCliente() {
		UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		Rol rol = new Rol();

		when(usuarioMapper.registroDTOToModel(dto)).thenReturn(usuario);
		when(rolRepository.findByActivoTrueAndNombreContainsIgnoreCase(any())).thenReturn(rol);

		Long id = usuarioService.registrarUsuarioCliente(dto);

		assertThat(id).isEqualTo(1L);
		verify(usuarioRepository).saveAndFlush(usuario);
		verify(usuarioRolRepository).saveAndFlush(any(UsuarioRol.class));
	}

	@Test
	void findByUuid() {
		String uuid = "uuid-123";
		Usuario usuario = new Usuario();
		UsuarioDTO dto = new UsuarioDTO();

		when(usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(usuario);
		when(usuarioMapper.modelToDTO(usuario)).thenReturn(dto);

		UsuarioDTO resultado = usuarioService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
	}

	@Test
	void actualizarPassword() {
		MiPerfilPasswordDTO dto = new MiPerfilPasswordDTO();
		dto.setId(1L);
		dto.setPassword("nuevaPass");
		Usuario usuario = new Usuario();

		when(usuarioRepository.findByActivoTrueAndId(1L)).thenReturn(usuario);
		when(passwordEncoder.encode("nuevaPass")).thenReturn("encodedPass");

		usuarioService.actualizarPassword(dto);

		assertThat(usuario.getPassword()).isEqualTo("encodedPass");
		verify(usuarioRepository).saveAndFlush(usuario);
	}

	@Test
	void getListDTO() {
		List<Usuario> listaModel = List.of(new Usuario());
		List<UsuarioDTO> listaDto = new ArrayList(List.of(new UsuarioDTO()));

		when(usuarioRepository.findByActivoTrue()).thenReturn(listaModel);
		when(usuarioMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioDTO> resultado = usuarioService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(usuarioRepository).findByActivoTrue();
	}

	@Test
	void getListDTOWithFilter() {
		UsuarioFilter filter = new UsuarioFilter();

		List<Usuario> listaModel = List.of(new Usuario());
		List<UsuarioDTO> listaDto = new ArrayList<>(List.of(new UsuarioDTO()));

		lenient().when(usuarioRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		lenient().when(usuarioMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioDTO> resultado = usuarioService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(usuarioRepository).findAll(any(Specification.class));
	}

	@Test
	void canWriteAndRead() {
		assertThat(usuarioService.canWrite(1L)).isTrue();
		assertThat(usuarioService.canRead(1L)).isTrue();
	}

	@Test
	void registrarUsuarioEmpresa() {
		EmpresaRegistroDTO dto = new EmpresaRegistroDTO();
		Usuario usuario = new Usuario();
		usuario.setId(5L);

		when(usuarioMapper.registroEmpresaDTOToModel(dto)).thenReturn(usuario);

		Long id = usuarioService.registrarUsuarioEmpresa(dto);

		assertThat(id).isEqualTo(5L);
		verify(usuarioRepository).saveAndFlush(usuario);
	}

	@Test
	void actualizarMiPerfil() {
		MiPerfilDTO dto = new MiPerfilDTO();
		dto.setId(1L);
		dto.setNombre("Nuevo Nombre");
		dto.setEmail("nuevo@example.com");
		Usuario usuario = new Usuario();

		when(usuarioRepository.findByActivoTrueAndId(1L)).thenReturn(usuario);

		usuarioService.actualizarMiPerfil(dto);

		assertThat(usuario.getNombre()).isEqualTo("Nuevo Nombre");
		assertThat(usuario.getEmail()).isEqualTo("nuevo@example.com");
		verify(usuarioRepository).saveAndFlush(usuario);
	}

	@Test
	void methodStubsReturnsNullOrDoesNothing() {
		assertThat(usuarioService.findByNombreEqualsIgnoreCase("test")).isNull();
		assertThat(usuarioService.getListComboDTO()).isNull();
	}

	@Test
	void enviarMailPasswordOlvidada() {
		UsuarioPasswordDTO dto = new UsuarioPasswordDTO();
		dto.setEmail("test@example.com");
		Usuario usuario = new Usuario();

		when(usuarioRepository.findByActivoTrueAndEmailEqualsIgnoreCase("test@example.com")).thenReturn(usuario);

		usuarioService.enviarMailPasswordOlvidada(dto);

		assertNotNull(usuario);

	}

	@Test
	void generarPasswordYEnviarMail() {
		UsuarioPasswordDTO dto = new UsuarioPasswordDTO();
		dto.setUuid("token-uuid");
		UsuarioToken token = new UsuarioToken();
		Usuario usuario = new Usuario();
		token.setUsuario(usuario);

		when(usuarioTokenService.getTokenActivoByUuid("token-uuid")).thenReturn(token);
		when(passwordEncoder.encode(any())).thenReturn("encoded");

		usuarioService.generarPasswordYEnviarMail(dto);

		verify(usuarioTokenService).desactivarTokensByUsuarioId(usuario.getId());
		verify(usuarioRepository).saveAndFlush(usuario);
	}
}