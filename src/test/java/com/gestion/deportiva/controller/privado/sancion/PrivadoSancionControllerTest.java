package com.gestion.deportiva.controller.privado.sancion;

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

import java.time.LocalDate;
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

import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.dto.filter.SancionFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.SancionService;
import com.gestion.deportiva.service.SancionTipoService;
import com.gestion.deportiva.util.Constantes;

@ExtendWith(MockitoExtension.class)
class PrivadoSancionControllerTest {

	private MockMvc mockMvc;

	@Mock
	private SancionService sancionService;

	@Mock
	private SancionTipoService sancionTipoService;

	@Mock
	private Validator validator;

	@InjectMocks
	private PrivadoSancionController privadoSancionController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(privadoSancionController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).setViewResolvers(viewResolver)
				.setValidator(validator).build();
	}

	@Test
	void shouldGetSolicitud() throws Exception {
		SancionDTO dto = new SancionDTO();
		dto.setReservaId(1L);
		when(sancionService.findByDTO(any(SancionDTO.class))).thenReturn(dto);
		when(sancionTipoService.getListDTO()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/privado/sancion/crear").param("reservaId", "1")).andExpect(status().isOk())
				.andExpect(view().name("privado/sancion/form"))
				.andExpect(model().attributeExists("form", "listSancionTipo"));
	}

	@Test
	void shouldGuardarSuccess() {
		when(sancionService.canWrite(anyLong())).thenReturn(true);
		when(sancionService.guardar(any(SancionDTO.class))).thenReturn(1L);

		try {
			mockMvc.perform(
					post("/privado/sancion/guardar").param("id", "1").param("reservaId", "1").param("usuarioId", "1")
							.param("sancionTipoId", "1").param("fechaInicio", LocalDate.now().toString())
							.param("fechaFin", LocalDate.now().toString()).param("motivo", "Test sanction"))
					.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/privado/sancion/1/editar"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(sancionService, times(1)).guardar(any(SancionDTO.class));
	}

	@Test
	void shouldGuardarThrowsPermisoException() {
		when(sancionService.canWrite(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(post("/privado/sancion/guardar").param("id", "1").param("reservaId", "1")
					.param("usuarioId", "1").param("sancionTipoId", "1")
					.param("fechaInicio", LocalDate.now().toString()).param("fechaFin", LocalDate.now().toString()))
					.andExpect(result -> assertNotNull(result.getResolvedException())).andExpect(
							result -> assertEquals(PermisoException.class, result.getResolvedException().getClass()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void shouldGuardarThrowsExceptionHandling() throws Exception {
		when(sancionService.canWrite(anyLong())).thenReturn(true);
		when(sancionService.guardar(any(SancionDTO.class))).thenThrow(new RuntimeException("Database error"));
		SancionDTO dto = new SancionDTO();
		when(sancionService.findByDTO(any(SancionDTO.class))).thenReturn(dto);
		when(sancionTipoService.getListDTO()).thenReturn(Collections.emptyList());

		mockMvc.perform(post("/privado/sancion/guardar").param("id", "1").param("reservaId", "1")
				.param("usuarioId", "1").param("sancionTipoId", "1").param("fechaInicio", LocalDate.now().toString())
				.param("fechaFin", LocalDate.now().toString())).andExpect(status().isOk())
				.andExpect(view().name("privado/sancion/form"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	@Test
	void shouldSearch() throws Exception {
		Page<SancionDTO> page = new PageImpl<>(Collections.emptyList());
		when(sancionService.getPageByFilter(any(SancionFilter.class), any(Pageable.class))).thenReturn(page);
		when(sancionTipoService.getListDTO()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/privado/sancion")).andExpect(status().isOk())
				.andExpect(view().name("privado/sancion/list"))
				.andExpect(model().attributeExists("page", "filter", "url", "breadcrumbs", "listSancionTipo"));
	}

	@Test
	void shouldSearchMisSanciones() throws Exception {
		Page<SancionDTO> page = new PageImpl<>(Collections.emptyList());
		when(sancionService.getPageByFilter(any(SancionFilter.class), any(Pageable.class))).thenReturn(page);
		when(sancionTipoService.getListDTO()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/privado/sancion/missanciones")).andExpect(status().isOk())
				.andExpect(view().name("privado/sancion/list"))
				.andExpect(model().attributeExists("page", "filter", "url", "breadcrumbs", "listSancionTipo"));
	}

	@Test
	void shouldAprobarAnularSuccess() {
		when(sancionService.canWrite(anyLong())).thenReturn(true);

		try {
			mockMvc.perform(get("/privado/sancion/1/anular")).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/privado/sancion"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(sancionService, times(1)).eliminar(1L);
	}

	@Test
	void shouldAprobarAnularThrowsPermisoException() {
		when(sancionService.canWrite(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(get("/privado/sancion/1/anular")).andExpect(result -> {
				Exception ex = result.getResolvedException();
				assertNotNull(ex);
				assertEquals(PermisoException.class, ex.getClass());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void shouldAprobarAnularThrowsExceptionHandling() {
		when(sancionService.canWrite(anyLong())).thenReturn(true);
		doThrow(new RuntimeException("Error deleting")).when(sancionService).eliminar(anyLong());

		try {
			mockMvc.perform(get("/privado/sancion/1/anular")).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/privado/sancion?httpStatus=500"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}