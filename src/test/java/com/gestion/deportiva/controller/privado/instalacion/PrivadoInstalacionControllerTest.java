package com.gestion.deportiva.controller.privado.instalacion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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

import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.util.SecurityUtil;

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

	@InjectMocks
	private PrivadoInstalacionController privadoInstalacionController;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

		mockMvc = MockMvcBuilders.standaloneSetup(privadoInstalacionController)
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
		when(instalacionService.getPageByFilter(any(), any())).thenReturn(new PageImpl<>(List.of()));
		when(empresaService.getListDTOParaInstalacion()).thenReturn(List.of());
		when(instalacionTipoService.getListDTO()).thenReturn(List.of());

		mockMvc.perform(get("/privado/instalacion")).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/list")).andExpect(model().attributeExists("page"));
	}

	@Test
	void editarPermitidoTest() throws Exception {
		Long id = 1L;
		when(instalacionService.canRead(id)).thenReturn(true);
		when(instalacionService.findById(id)).thenReturn(new InstalacionDTO());

		mockMvc.perform(get("/privado/instalacion/{id}/editar", id)).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/form")).andExpect(model().attributeExists("form"));
	}

	@Test
	void eliminarExitosoTest() throws Exception {
		Long id = 1L;
		when(instalacionService.canRead(id)).thenReturn(true);

		mockMvc.perform(get("/privado/instalacion/{id}/eliminar", id)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/privado/instalacion"));

		verify(instalacionService).eliminar(id);
	}

	@Test
	void eliminarLanzaExcepcionInternaTest() throws Exception {
		Long id = 1L;
		when(instalacionService.canRead(id)).thenReturn(true);
		doThrow(new RuntimeException("Error DB")).when(instalacionService).eliminar(id);

		mockMvc.perform(get("/privado/instalacion/{id}/eliminar", id)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/privado/instalacion"));
	}

	@Test
	void crearTest() throws Exception {
		mockMvc.perform(get("/privado/instalacion/crear")).andExpect(status().isOk())
				.andExpect(view().name("privado/instalacion/form")).andExpect(model().attributeExists("form"));
	}

}