package com.gestion.deportiva.controller.privado.instalacion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.gestion.deportiva.service.InstalacionHorarioBloqueadoService;
import com.gestion.deportiva.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class PrivadoInstalacionHorarioBloqueadoControllerTest {

	private MockMvc mockMvc;

	@Mock
	private InstalacionHorarioBloqueadoService instalacionHorarioBloqueadoService;

	@InjectMocks
	private PrivadoInstalacionHorarioBloqueadoController controller;

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
	void searchTest() throws Exception {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
		when(instalacionHorarioBloqueadoService.getPageByFilter(any(), any())).thenReturn(new PageImpl<>(List.of()));

		mockMvc.perform(get("/privado/instalacion/1/horario/bloqueado")).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/horarioBloqueadoList"))
				.andExpect(model().attributeExists("page"));
	}

	@Test
	void eliminarExitosoTest() throws Exception {
		Long idInstalacion = 1L;
		Long id = 10L;
		when(instalacionHorarioBloqueadoService.canWrite(id)).thenReturn(true);

		mockMvc.perform(get("/privado/instalacion/{idInstalacion}/horario/bloqueado/{id}/eliminar", idInstalacion, id))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/privado/instalacion/1/horario/bloqueado"));

		verify(instalacionHorarioBloqueadoService).eliminar(id);
	}

}