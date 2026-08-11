package com.gestion.deportiva.controller.privado.empleado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gestion.deportiva.dto.EmpleadoRolDTO;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.EmpleadoService;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.service.RolService;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.service.UsuarioRolService;
import com.gestion.deportiva.util.Constantes;

@ExtendWith(MockitoExtension.class)
class PrivadoEmpleadoRolControllerTest {

	private MockMvc mockMvc;

	@Mock
	private EmpleadoService empleadoService;

	@Mock
	private UsuarioRolService usuarioRolService;

	@Mock
	private EmpresaService empresaService;

	@Mock
	private SedeService sedeService;

	@Mock
	private InstalacionService instalacionService;

	@Mock
	private RolService rolService;

	@Mock
	private Validator validator;

	@InjectMocks
	private PrivadoEmpleadoRolController privadoEmpleadoRolController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(privadoEmpleadoRolController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).setViewResolvers(viewResolver)
				.setValidator(validator).build();
	}

	@Test
	void shouldVer() throws Exception {
		EmpleadoRolDTO dto = new EmpleadoRolDTO();
		dto.setUsuarioId(1L);
		when(usuarioRolService.canWrite(anyLong())).thenReturn(true);
		when(empleadoService.getEmpleadoRolDTO(anyLong())).thenReturn(dto);
		when(rolService.getListDTOParaEmpleado()).thenReturn(Collections.emptyList());
		when(empresaService.getListDTOParaEmpleado()).thenReturn(Collections.emptyList());
		when(sedeService.getListDTOParaEmpleado(isNull())).thenReturn(Collections.emptyList());
		when(instalacionService.getListDTOParaEmpleado(isNull(), isNull())).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/privado/empleado/1/rol")).andExpect(status().isOk())
				.andExpect(view().name("privado/empleado/rolForm")).andExpect(model().attributeExists("form",
						"listRoles", "listEmpresas", "listSedes", "listInstalaciones", "breadcrumbs"));
	}

	@Test
	void shouldVerThrowsPermisoException() {
		when(usuarioRolService.canWrite(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(get("/privado/empleado/1/rol")).andExpect(result -> {
				Exception ex = result.getResolvedException();
				assertNotNull(ex);
				assertEquals(PermisoException.class, ex.getClass());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void shouldGuardarSuccess() {
		when(usuarioRolService.canWrite(anyLong())).thenReturn(true);

		try {
			mockMvc.perform(post("/privado/empleado/1/rol/guardar").param("id", "1").param("usuarioId", "1"))
					.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/privado/empleado/1/rol"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(empleadoService, times(1)).guardarEmpleadoRol(any(EmpleadoRolDTO.class));
	}

	@Test
	void shouldGuardarThrowsPermisoException() {
		when(usuarioRolService.canWrite(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(post("/privado/empleado/1/rol/guardar").param("id", "1").param("usuarioId", "1"))
					.andExpect(result -> {
						Exception ex = result.getResolvedException();
						assertNotNull(ex);
						assertEquals(PermisoException.class, ex.getClass());
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void shouldGuardarThrowsExceptionHandling() throws Exception {
		when(usuarioRolService.canWrite(anyLong())).thenReturn(true);
		doThrow(new RuntimeException("Database error")).when(empleadoService)
				.guardarEmpleadoRol(any(EmpleadoRolDTO.class));
		when(rolService.getListDTOParaEmpleado()).thenReturn(Collections.emptyList());
		when(empresaService.getListDTOParaEmpleado()).thenReturn(Collections.emptyList());
		when(sedeService.getListDTOParaEmpleado(isNull())).thenReturn(Collections.emptyList());
		when(instalacionService.getListDTOParaEmpleado(isNull(), isNull())).thenReturn(Collections.emptyList());

		mockMvc.perform(post("/privado/empleado/1/rol/guardar").param("id", "1").param("usuarioId", "1"))
				.andExpect(status().isOk()).andExpect(view().name("privado/empleado/rolForm"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
}