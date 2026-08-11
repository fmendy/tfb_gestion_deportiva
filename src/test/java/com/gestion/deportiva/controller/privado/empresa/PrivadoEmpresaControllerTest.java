package com.gestion.deportiva.controller.privado.empresa;

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

import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.util.Constantes;

@ExtendWith(MockitoExtension.class)
class PrivadoEmpresaControllerTest {

	private MockMvc mockMvc;

	@Mock
	private EmpresaService empresaService;

	@Mock
	private Validator validator;

	@InjectMocks
	private PrivadoEmpresaController privadoEmpresaController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(privadoEmpresaController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).setViewResolvers(viewResolver)
				.setValidator(validator).build();
	}

	@Test
	void shouldSearch() throws Exception {
		Page<EmpresaDTO> page = new PageImpl<>(Collections.emptyList());
		when(empresaService.getPageByFilter(any(EmpresaFilter.class), any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/privado/empresa")).andExpect(status().isOk())
				.andExpect(view().name("privado/empresa/list"))
				.andExpect(model().attributeExists("page", "filter", "url", "breadcrumbs"));
	}

	@Test
	void shouldEditar() throws Exception {
		when(empresaService.canRead(anyLong())).thenReturn(true);
		when(empresaService.findById(anyLong())).thenReturn(new EmpresaDTO());

		mockMvc.perform(get("/privado/empresa/1/editar")).andExpect(status().isOk())
				.andExpect(view().name("privado/empresa/form"))
				.andExpect(model().attributeExists("form", "breadcrumbs"));
	}

	@Test
	void shouldEditarThrowsPermisoException() {
		when(empresaService.canRead(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(get("/privado/empresa/1/editar")).andExpect(result -> {
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
		when(empresaService.canWrite(anyLong())).thenReturn(true);
		when(empresaService.guardar(any(EmpresaDTO.class))).thenReturn(1L);

		try {
			mockMvc.perform(post("/privado/empresa/guardar").param("id", "1").param("nombre", "Empresa Test"))
					.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/privado/empresa/1/editar"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(empresaService, times(1)).guardar(any(EmpresaDTO.class));
	}

	@Test
	void shouldGuardarThrowsPermisoException() {
		when(empresaService.canWrite(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(post("/privado/empresa/guardar").param("id", "1").param("nombre", "Empresa Test"))
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
		when(empresaService.canWrite(anyLong())).thenReturn(true);
		doThrow(new RuntimeException("Database error")).when(empresaService).guardar(any(EmpresaDTO.class));
		when(empresaService.findById(anyLong())).thenReturn(new EmpresaDTO());

		mockMvc.perform(post("/privado/empresa/guardar").param("id", "1").param("nombre", "Empresa Test"))
				.andExpect(status().isOk()).andExpect(view().name("privado/empresa/form"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
}