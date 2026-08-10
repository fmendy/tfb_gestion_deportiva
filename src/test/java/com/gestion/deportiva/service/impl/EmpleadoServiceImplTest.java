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
import com.gestion.deportiva.dto.EmpleadoRegistroDTO;
import com.gestion.deportiva.dto.filter.EmpleadoFilter;
import com.gestion.deportiva.mapper.EmpleadoMapper;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.SedeRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.util.Constantes;
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
	
	@Mock
	private SedeRepository sedeRepository;

	@Mock
	private InstalacionRepository instalacionRepository;

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
	
	@Test
	void obtenerPaginaPorFiltroConDiferentesPermisosTest() {
		EmpleadoFilter filter = new EmpleadoFilter();
		Pageable pageable = PageRequest.of(0, 10);
		Usuario usuario = new Usuario();
		Page<Usuario> pageUsuario = new PageImpl<>(List.of(usuario));
		Page<EmpleadoDTO> pageDto = new PageImpl<>(List.of(new EmpleadoDTO()));

		when(usuarioRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageUsuario);
		when(empleadoMapper.pageToPageDTO(pageUsuario)).thenReturn(pageDto);

		// 1. GESTION_USUARIO_EMPRESA
		securityUtilMockedStatic.when(SecurityUtil::hasGlobalAccess).thenReturn(false);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)).thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListEmpresaId).thenReturn(List.of(1L));

		empleadoService.getPageByFilter(filter, pageable);

		// 2. GESTION_USUARIO_SEDE
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)).thenReturn(false);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)).thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListSedeId).thenReturn(List.of(1L));

		empleadoService.getPageByFilter(filter, pageable);

		// 3. GESTION_USUARIO_INSTALACION
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)).thenReturn(false);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION)).thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListInstalacionId).thenReturn(List.of(1L));

		empleadoService.getPageByFilter(filter, pageable);

		// 4. Sin permisos (Else -> -1L)
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION)).thenReturn(false);
		
		Page<EmpleadoDTO> resultado = empleadoService.getPageByFilter(filter, pageable);
		assertThat(resultado).isNotNull();
	}

	@Test
	void canWriteYCanReadScenariosTest() {
		Long usuarioId = 1L;
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);

		// GESTION_USUARIO_GLOBAL
		securityUtilMockedStatic.when(SecurityUtil::hasGlobalAccess).thenReturn(false);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_GLOBAL)).thenReturn(true);
		assertThat(empleadoService.canWrite(usuarioId)).isTrue();
		assertThat(empleadoService.canRead(usuarioId)).isTrue();

		// Creación (id == null) con permisos de gestión
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_GLOBAL)).thenReturn(false);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAnyAuthority(any(), any(), any())).thenReturn(true);
		assertThat(empleadoService.canWrite(null)).isTrue();

		// Usuario null
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAnyAuthority(any(), any(), any())).thenReturn(false);
		when(usuarioRepository.findByActivoTrueAndId(usuarioId)).thenReturn(null);
		assertThat(empleadoService.canWrite(usuarioId)).isFalse();

		// Acceso por Empresa
		when(usuarioRepository.findByActivoTrueAndId(usuarioId)).thenReturn(usuario);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)).thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListEmpresaId).thenReturn(List.of(10L));
		
		com.gestion.deportiva.model.UsuarioEmpresa ue = new com.gestion.deportiva.model.UsuarioEmpresa();
		com.gestion.deportiva.model.Empresa emp = new com.gestion.deportiva.model.Empresa();
		emp.setId(10L);
		ue.setEmpresa(emp);
		usuario.setListUsuarioEmpresa(List.of(ue));
		
		assertThat(empleadoService.canWrite(usuarioId)).isTrue();

		// Acceso por Sede
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)).thenReturn(false);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)).thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListSedeId).thenReturn(List.of(20L));
		
		com.gestion.deportiva.model.UsuarioSede us = new com.gestion.deportiva.model.UsuarioSede();
		com.gestion.deportiva.model.Sede sede = new com.gestion.deportiva.model.Sede();
		sede.setId(20L);
		us.setSede(sede);
		usuario.setListUsuarioSede(List.of(us));
		usuario.setListUsuarioEmpresa(List.of());

		assertThat(empleadoService.canWrite(usuarioId)).isTrue();

		// Acceso por Instalación
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)).thenReturn(false);
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION)).thenReturn(true);
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserListInstalacionId).thenReturn(List.of(30L));
		
		com.gestion.deportiva.model.UsuarioInstalacion ui = new com.gestion.deportiva.model.UsuarioInstalacion();
		com.gestion.deportiva.model.Instalacion inst = new com.gestion.deportiva.model.Instalacion();
		inst.setId(30L);
		ui.setInstalacion(inst);
		usuario.setListUsuarioInstalacion(List.of(ui));
		usuario.setListUsuarioSede(List.of());

		assertThat(empleadoService.canWrite(usuarioId)).isTrue();
	}

	@Test
	void encontrarEmpleadoRegistroYMetodosVaciosTest() {
		Long id = 1L;
		Usuario usuario = new Usuario();
		EmpleadoRegistroDTO registroDTO = new EmpleadoRegistroDTO();

		when(usuarioRepository.findByActivoTrueAndId(id)).thenReturn(usuario);
		when(empleadoMapper.modelToEmpleadoRegistroDTO(usuario)).thenReturn(registroDTO);

		EmpleadoRegistroDTO resultado = empleadoService.findEmpleadoRegistroById(id);

		assertThat(resultado).isEqualTo(registroDTO);
		
		// Verificación de métodos sin lógica compleja
		empleadoService.eliminar(id);
		assertThat(empleadoService.getListComboDTO()).isNull();
		assertThat(empleadoService.getListDTO()).isNull();
		assertThat(empleadoService.getListDTO(new EmpleadoFilter())).isNull();
	}
	
	
}