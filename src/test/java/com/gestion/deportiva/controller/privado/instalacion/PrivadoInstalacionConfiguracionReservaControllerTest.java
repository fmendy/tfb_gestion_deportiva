package com.gestion.deportiva.controller.privado.instalacion;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.gestion.deportiva.dto.InstalacionConfiguracionReservaDTO;
import com.gestion.deportiva.service.InstalacionConfiguracionReservaService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class PrivadoInstalacionConfiguracionReservaControllerTest {

	private MockMvc mockMvc;

	@Mock
	private InstalacionConfiguracionReservaService instalacionConfiguracionReservaService;

	@Mock
	private InstalacionService instalacionService;

	@InjectMocks
	private PrivadoInstalacionConfiguracionReservaController controller;

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
		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();
		dto.setInstalacionId(idInstalacion);

		when(instalacionConfiguracionReservaService.findDTOByInstalacionIdOrNewIfEmpty(idInstalacion)).thenReturn(dto);

		mockMvc.perform(get("/privado/instalacion/{idInstalacion}/configuracion/reserva", idInstalacion))
				.andExpect(status().isOk()).andExpect(view().name("privado/instalacion/configuracionReservaForm"))
				.andExpect(model().attributeExists("form"));
	}

}