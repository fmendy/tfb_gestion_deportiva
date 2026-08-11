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

import com.gestion.deportiva.dto.InstalacionHorarioEspecialDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioEspecialFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.InstalacionHorarioEspecialService;
import com.gestion.deportiva.util.Constantes;

@ExtendWith(MockitoExtension.class)
class PrivadoInstalacionHorarioEspecialControllerTest {

	private MockMvc mockMvc;

	@Mock
	private InstalacionHorarioEspecialService instalacionHorarioEspecialService;

	@Mock
	private Validator validator;

	@InjectMocks
	private PrivadoInstalacionHorarioEspecialController privadoInstalacionHorarioEspecialController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(privadoInstalacionHorarioEspecialController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).setViewResolvers(viewResolver)
				.setValidator(validator).build();
	}

	@Test
	void shouldSearch() throws Exception {
		Page<InstalacionHorarioEspecialDTO> page = new PageImpl<>(Collections.emptyList());
		when(instalacionHorarioEspecialService.getPageByFilter(any(InstalacionHorarioEspecialFilter.class),
				any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/privado/instalacion/1/horario/especial")).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/horarioEspecialList"))
				.andExpect(model().attributeExists("page", "filter", "url", "breadcrumbs"));
	}

	@Test
	void shouldEditar() throws Exception {
		InstalacionHorarioEspecialDTO dto = new InstalacionHorarioEspecialDTO();
		dto.setInstalacionId(1L);
		when(instalacionHorarioEspecialService.canRead(anyLong())).thenReturn(true);
		when(instalacionHorarioEspecialService.findByIdOrNewEmpty(anyLong(), anyLong())).thenReturn(dto);

		mockMvc.perform(get("/privado/instalacion/1/horario/especial/1/editar")).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/horarioEspecialForm"))
				.andExpect(model().attributeExists("form", "breadcrumbs"));
	}

	@Test
	void shouldEditarThrowsPermisoException() {
		when(instalacionHorarioEspecialService.canRead(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(get("/privado/instalacion/1/horario/especial/1/editar")).andExpect(result -> {
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
		InstalacionHorarioEspecialDTO dto = new InstalacionHorarioEspecialDTO();
		dto.setInstalacionId(1L);
		when(instalacionHorarioEspecialService.findByIdOrNewEmpty(any(), anyLong())).thenReturn(dto);

		mockMvc.perform(get("/privado/instalacion/1/horario/especial/crear")).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/horarioEspecialForm"))
				.andExpect(model().attributeExists("form", "breadcrumbs"));
	}

	@Test
	void shouldGuardarSuccess() {
		when(instalacionHorarioEspecialService.canWrite(anyLong())).thenReturn(true);

		try {
			mockMvc.perform(post("/privado/instalacion/1/horario/especial/guardar").param("id", "1")
					.param("instalacionId", "1").param("fechaInicio", LocalDate.now().toString())
					.param("fechaFin", LocalDate.now().toString())).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/privado/instalacion/1/horario/especial"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(instalacionHorarioEspecialService, times(1)).guardar(any(InstalacionHorarioEspecialDTO.class));
	}

	@Test
	void shouldGuardarThrowsPermisoException() {
		when(instalacionHorarioEspecialService.canWrite(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(post("/privado/instalacion/1/horario/especial/guardar").param("id", "1")
					.param("instalacionId", "1")).andExpect(result -> {
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
		when(instalacionHorarioEspecialService.canWrite(anyLong())).thenReturn(true);
		doThrow(new RuntimeException("Database error")).when(instalacionHorarioEspecialService)
				.guardar(any(InstalacionHorarioEspecialDTO.class));

		mockMvc.perform(
				post("/privado/instalacion/1/horario/especial/guardar").param("id", "1").param("instalacionId", "1")
						.param("fechaInicio", LocalDate.now().toString()).param("fechaFin", LocalDate.now().toString()))
				.andExpect(status().isOk()).andExpect(view().name("privado/instalacion/horarioEspecialForm"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	@Test
	void shouldEliminarSuccess() {
		when(instalacionHorarioEspecialService.canWrite(anyLong())).thenReturn(true);

		try {
			mockMvc.perform(get("/privado/instalacion/1/horario/especial/1/eliminar"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/privado/instalacion/1/horario/especial"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(instalacionHorarioEspecialService, times(1)).eliminar(1L);
	}

	@Test
	void shouldEliminarThrowsPermisoException() {
		when(instalacionHorarioEspecialService.canWrite(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(get("/privado/instalacion/1/horario/especial/1/eliminar")).andExpect(result -> {
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
		when(instalacionHorarioEspecialService.canWrite(anyLong())).thenReturn(true);
		doThrow(new RuntimeException("Error deleting")).when(instalacionHorarioEspecialService).eliminar(anyLong());

		try {
			mockMvc.perform(get("/privado/instalacion/1/horario/especial/1/eliminar"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/privado/instalacion/1/horario/especial?httpStatus=500"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}