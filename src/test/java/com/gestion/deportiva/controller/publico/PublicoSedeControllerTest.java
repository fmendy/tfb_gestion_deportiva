package com.gestion.deportiva.controller.publico;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gestion.deportiva.dto.SedePublicoDTO;
import com.gestion.deportiva.dto.filter.SedePublicoFilter;
import com.gestion.deportiva.service.ComunidadAutonomaService;
import com.gestion.deportiva.service.MunicipioService;
import com.gestion.deportiva.service.ProvinciaService;
import com.gestion.deportiva.service.SedeService;

@ExtendWith(MockitoExtension.class)
class PublicoSedeControllerTest {

	private MockMvc mockMvc;

	@Mock
	private SedeService sedeService;

	@Mock
	private ComunidadAutonomaService comunidadAutonomaService;

	@Mock
	private ProvinciaService provinciaService;

	@Mock
	private MunicipioService municipioService;

	@InjectMocks
	private PublicoSedeController publicoSedeController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(publicoSedeController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).setViewResolvers(viewResolver)
				.build();
	}

	@Test
	void shouldMapa() throws Exception {
		when(sedeService.getListSedePublicoDTO(any(SedePublicoFilter.class))).thenReturn(Collections.emptyList());
		when(comunidadAutonomaService.getListDTO()).thenReturn(Collections.emptyList());
		when(provinciaService.getListDTOByComunidadAutonomaId(any())).thenReturn(Collections.emptyList());
		when(municipioService.getListDTOByComunidadAutonomaIdOrProvinciaId(any(), any()))
				.thenReturn(Collections.emptyList());

		mockMvc.perform(get("/publico/sede/mapa")).andExpect(status().isOk())
				.andExpect(view().name("publico/sede/mapaList")).andExpect(model().attributeExists("filter",
						"listSedeMapa", "listComunidades", "listProvincias", "listMunicipios"));
	}

	@Test
	void shouldDetalle() throws Exception {
		when(sedeService.getSedePublicoDTOById(anyLong())).thenReturn(new SedePublicoDTO());

		mockMvc.perform(get("/publico/sede/1/detalle")).andExpect(status().isOk())
				.andExpect(view().name("publico/sede/form")).andExpect(model().attributeExists("sede"));
	}
}