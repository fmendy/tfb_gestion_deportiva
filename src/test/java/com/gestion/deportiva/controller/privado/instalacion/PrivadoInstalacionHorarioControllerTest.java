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

import com.gestion.deportiva.dto.InstalacionHorarioSemanalDTO;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.InstalacionHorarioService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.util.Constantes;

@ExtendWith(MockitoExtension.class)
class PrivadoInstalacionHorarioControllerTest {

	private MockMvc mockMvc;

	@Mock
	private InstalacionHorarioService instalacionHorarioService;

	@Mock
	private InstalacionService instalacionService;

	@Mock
	private Validator validator;

	@InjectMocks
	private PrivadoInstalacionHorarioController privadoInstalacionHorarioController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(privadoInstalacionHorarioController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.setViewResolvers(viewResolver)
				.setValidator(validator)
				.build();
	}

	@Test
	void shouldVer() throws Exception {
		InstalacionHorarioSemanalDTO dto = new InstalacionHorarioSemanalDTO();
		dto.setInstalacionId(1L);
		when(instalacionHorarioService.cargarHorarioSemanal(anyLong())).thenReturn(dto);

		mockMvc.perform(get("/privado/instalacion/1/horario"))
				.andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/horarioForm"))
				.andExpect(model().attributeExists("form", "breadcrumbs"));
	}

	@Test
	void shouldGuardarSuccess() {
		when(instalacionService.canWrite(anyLong())).thenReturn(true);

		try {
			mockMvc.perform(post("/privado/instalacion/1/horario/guardar")
					.param("instalacionId", "1"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/privado/instalacion/1/horario"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(instalacionHorarioService, times(1)).guardar(any(InstalacionHorarioSemanalDTO.class));
	}

	@Test
	void shouldGuardarThrowsPermisoException() {
		when(instalacionService.canWrite(anyLong())).thenReturn(false);

		try {
			mockMvc.perform(post("/privado/instalacion/1/horario/guardar")
					.param("instalacionId", "1"))
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
		doThrow(new RuntimeException("Database error")).when(instalacionHorarioService).guardar(any(InstalacionHorarioSemanalDTO.class));

		mockMvc.perform(post("/privado/instalacion/1/horario/guardar")
				.param("instalacionId", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/horarioForm"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
}