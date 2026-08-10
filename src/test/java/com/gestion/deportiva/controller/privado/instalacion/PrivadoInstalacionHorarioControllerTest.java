package com.gestion.deportiva.controller.privado.instalacion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.gestion.deportiva.dto.InstalacionHorarioSemanalDTO;
import com.gestion.deportiva.service.InstalacionHorarioService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class PrivadoInstalacionHorarioControllerTest {

	private MockMvc mockMvc;

	@Mock
	private InstalacionHorarioService instalacionHorarioService;

	@Mock
	private InstalacionService instalacionService;

	@InjectMocks
	private PrivadoInstalacionHorarioController controller;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).setValidator(validator)
				.build();

		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		securityUtilMockedStatic.close();
	}

	@Test
	void verTest() throws Exception {
		Long idInstalacion = 1L;
		InstalacionHorarioSemanalDTO dto = new InstalacionHorarioSemanalDTO();
		dto.setInstalacionId(idInstalacion);

		when(instalacionHorarioService.cargarHorarioSemanal(idInstalacion)).thenReturn(dto);

		mockMvc.perform(get("/privado/instalacion/{idInstalacion}/horario", idInstalacion)).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/horarioForm")).andExpect(model().attributeExists("form"));
	}

	@Test
	void guardarExitosoTest() throws Exception {
		InstalacionHorarioSemanalDTO dto = new InstalacionHorarioSemanalDTO();
		dto.setId(1L);
		dto.setInstalacionId(1L);

		when(instalacionService.canWrite(1L)).thenReturn(true);

		mockMvc.perform(post("/privado/instalacion/1/horario/guardar").flashAttr("form", dto))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/privado/instalacion/1/horario"));

		verify(instalacionHorarioService).guardar(any(InstalacionHorarioSemanalDTO.class));
	}

	@Test
	void guardarLanzaExcepcionTest() throws Exception {
		InstalacionHorarioSemanalDTO dto = new InstalacionHorarioSemanalDTO();
		dto.setId(1L);
		dto.setInstalacionId(1L);

		when(instalacionService.canWrite(1L)).thenReturn(true);
		doThrow(new RuntimeException("Error al guardar")).when(instalacionHorarioService)
				.guardar(any(InstalacionHorarioSemanalDTO.class));

		mockMvc.perform(post("/privado/instalacion/1/horario/guardar").flashAttr("form", dto))
				.andExpect(status().isOk()).andExpect(view().name("privado/instalacion/horarioForm"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
}