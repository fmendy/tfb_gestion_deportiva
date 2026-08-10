package com.gestion.deportiva.controller.privado.sede;

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
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.ComunidadAutonomaService;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.MunicipioService;
import com.gestion.deportiva.service.ProvinciaService;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class PrivadoSedeControllerTest {

	private MockMvc mockMvc;

	@Mock
	private EmpresaService empresaService;

	@Mock
	private SedeService sedeService;

	@Mock
	private ComunidadAutonomaService comunidadAutonomaService;

	@Mock
	private ProvinciaService provinciaService;

	@Mock
	private MunicipioService municipioService;

	@InjectMocks
	private PrivadoSedeController privadoSedeController;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

		mockMvc = MockMvcBuilders.standaloneSetup(privadoSedeController)
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
		when(sedeService.getPageByFilter(any(), any())).thenReturn(new PageImpl<>(List.of()));
		when(empresaService.getListDTO(any())).thenReturn(List.of());
		when(comunidadAutonomaService.getListDTO()).thenReturn(List.of());

		mockMvc.perform(get("/privado/sede")).andExpect(status().isOk()).andExpect(view().name("privado/sede/list"))
				.andExpect(model().attributeExists("page"));
	}

	@Test
	void editarPermitidoTest() throws Exception {
		Long id = 1L;
		when(sedeService.canRead(id)).thenReturn(true);
		when(sedeService.findById(id)).thenReturn(new SedeDTO());

		mockMvc.perform(get("/privado/sede/{id}/editar", id)).andExpect(status().isOk())
				.andExpect(view().name("privado/sede/form")).andExpect(model().attributeExists("form"));
	}

	@Test
	void eliminarExitosoTest() throws Exception {
		Long id = 1L;
		when(sedeService.canRead(id)).thenReturn(true);

		mockMvc.perform(get("/privado/sede/{id}/eliminar", id)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/privado/sede"));

		verify(sedeService).eliminar(id);
	}

	@Test
	void eliminarLanzaExcepcionInternaTest() throws Exception {
		Long id = 1L;
		when(sedeService.canRead(id)).thenReturn(true);
		doThrow(new RuntimeException("Error DB")).when(sedeService).eliminar(id);

		mockMvc.perform(get("/privado/sede/{id}/eliminar", id)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/privado/sede"));
	}

	@Test
	void crearTest() throws Exception {
		mockMvc.perform(get("/privado/sede/crear")).andExpect(status().isOk())
				.andExpect(view().name("privado/sede/form")).andExpect(model().attributeExists("form"));
	}


}