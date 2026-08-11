package com.gestion.deportiva.controller.privado.instalacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.filter.InstalacionFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.util.Constantes;

@ExtendWith(MockitoExtension.class)
class PrivadoInstalacionControllerTest {

	private MockMvc mockMvc;

	@Mock
	private InstalacionService instalacionService;

	@Mock
	private SedeService sedeService;

	@Mock
	private InstalacionTipoService instalacionTipoService;

	@Mock
	private EmpresaService empresaService;

	@Mock
	private Validator validator;

	@InjectMocks
	private PrivadoInstalacionController privadoInstalacionController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(privadoInstalacionController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).setViewResolvers(viewResolver)
				.setValidator(validator).build();
	}

	@Test
	void shouldSearch() throws Exception {
		Page<InstalacionDTO> page = new PageImpl<>(Collections.emptyList());
		when(instalacionService.getPageByFilter(any(InstalacionFilter.class), any(Pageable.class))).thenReturn(page);
		when(empresaService.getListDTOParaInstalacion()).thenReturn(Collections.emptyList());
		when(instalacionTipoService.getListDTO()).thenReturn(Collections.emptyList());
		when(sedeService.getListDTOParaInstalacion(any())).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/privado/instalacion")).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/list")).andExpect(model().attributeExists("page", "filter",
						"url", "listEmpresas", "listInstalacionTipo", "listSedes", "breadcrumbs"));
	}

	@Test
	void shouldEditar() throws Exception {
		when(instalacionService.canRead(anyLong())).thenReturn(true);
		when(instalacionService.findById(anyLong())).thenReturn(new InstalacionDTO());
		when(empresaService.getListDTOParaInstalacion()).thenReturn(Collections.emptyList());
		when(instalacionTipoService.getListDTO()).thenReturn(Collections.emptyList());
		when(sedeService.getListDTOParaInstalacion(any())).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/privado/instalacion/1/editar")).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/form")).andExpect(model().attributeExists("form",
						"listEmpresas", "listSedes", "listInstalacionTipo", "breadcrumbs"));
	}

	@Test
	void shouldEditarThrowsPermisoException() {
		when(instalacionService.canRead(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(get("/privado/instalacion/1/editar")).andExpect(result -> {
				Exception ex = result.getResolvedException();
				assertNotNull(ex);
				assertEquals(PermisoException.class, ex.getClass());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void shouldCrear() throws Exception {
		when(empresaService.getListDTOParaInstalacion()).thenReturn(Collections.emptyList());
		when(instalacionTipoService.getListDTO()).thenReturn(Collections.emptyList());
		when(sedeService.getListDTOParaInstalacion(any())).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/privado/instalacion/crear")).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/form")).andExpect(model().attributeExists("form",
						"listEmpresas", "listSedes", "listInstalacionTipo", "breadcrumbs"));
	}

	@Test
	void shouldGuardarSuccess() {
		when(instalacionService.canWrite(anyLong())).thenReturn(true);
		when(instalacionService.guardar(any(InstalacionDTO.class))).thenReturn(1L);

		try {
			mockMvc.perform(post("/privado/instalacion/guardar").param("id", "1").param("nombre", "Instalacion Test"))
					.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/privado/instalacion/1/editar"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(instalacionService, times(1)).guardar(any(InstalacionDTO.class));
	}

	@Test
	void shouldGuardarThrowsPermisoException() {
		when(instalacionService.canWrite(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(post("/privado/instalacion/guardar").param("id", "1").param("nombre", "Instalacion Test"))
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
		when(instalacionService.canWrite(anyLong())).thenReturn(true);
		doThrow(new RuntimeException("Database error")).when(instalacionService).guardar(any(InstalacionDTO.class));
		when(empresaService.getListDTOParaInstalacion()).thenReturn(Collections.emptyList());
		when(instalacionTipoService.getListDTO()).thenReturn(Collections.emptyList());
		when(sedeService.getListDTOParaInstalacion(any())).thenReturn(Collections.emptyList());

		mockMvc.perform(post("/privado/instalacion/guardar").param("id", "1").param("nombre", "Instalacion Test"))
				.andExpect(status().isOk()).andExpect(view().name("privado/instalacion/form"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	@Test
	void shouldEliminarSuccess() {
		when(instalacionService.canRead(anyLong())).thenReturn(true);

		try {
			mockMvc.perform(get("/privado/instalacion/1/eliminar")).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/privado/instalacion"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(instalacionService, times(1)).eliminar(1L);
	}

	@Test
	void shouldEliminarThrowsPermisoException() {
		when(instalacionService.canRead(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(get("/privado/instalacion/1/eliminar")).andExpect(result -> {
				Exception ex = result.getResolvedException();
				assertNotNull(ex);
				assertEquals(PermisoException.class, ex.getClass());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void shouldEliminarThrowsExceptionHandling() {
		when(instalacionService.canRead(anyLong())).thenReturn(true);
		doThrow(new RuntimeException("Error deleting")).when(instalacionService).eliminar(anyLong());

		try {
			mockMvc.perform(get("/privado/instalacion/1/eliminar")).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/privado/instalacion"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}