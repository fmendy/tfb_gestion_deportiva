package com.gestion.deportiva.controller.publico;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.InstalacionDisponibilidadDTO;
import com.gestion.deportiva.dto.InstalacionPublicoDTO;
import com.gestion.deportiva.dto.filter.InstalacionPublicoFilter;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.service.MunicipioService;

@ExtendWith(MockitoExtension.class)
class PublicoInstalacionControllerTest {

	private MockMvc mockMvc;

	@Mock
	private InstalacionService instalacionService;

	@Mock
	private InstalacionTipoService instalacionTipoService;

	@Mock
	private MunicipioService municipioService;

	@InjectMocks
	private PublicoInstalacionController publicoInstalacionController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(publicoInstalacionController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).setViewResolvers(viewResolver)
				.build();
	}

	@Test
	void shouldGetDetalle() throws Exception {
		when(instalacionService.getPublicoDTOById(anyLong())).thenReturn(new InstalacionPublicoDTO());

		mockMvc.perform(get("/publico/instalacion/1/detalle")).andExpect(status().isOk())
				.andExpect(view().name("publico/instalacion/form")).andExpect(model().attributeExists("instalacion"));
	}

	@Test
	void shouldSearch() throws Exception {
		List<InstalacionDTO> page = new ArrayList<>(Collections.emptyList());
		when(instalacionService.getListByFilter(any(InstalacionPublicoFilter.class), any(Pageable.class)))
				.thenReturn(page);
		when(instalacionTipoService.getListDTO()).thenReturn(Collections.emptyList());
		when(municipioService.getListDTOConSedes()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/publico/instalacion/buscar")).andExpect(status().isOk())
				.andExpect(view().name("publico/instalacion/list")).andExpect(model().attributeExists("listInstalacion",
						"filter", "url", "listInstalacionTipo", "listMunicipio"));
	}

	@Test
	void shouldMostrarDisponibilidad() throws Exception {
		when(instalacionService.getDisponibilidadDTOById(anyLong(), any(LocalDate.class)))
				.thenReturn(new InstalacionDisponibilidadDTO());

		mockMvc.perform(get("/publico/instalacion/1/disponibilidad").param("fecha", LocalDate.now().toString()))
				.andExpect(status().isOk()).andExpect(view().name("publico/instalacion/disponibilidadForm"))
				.andExpect(model().attributeExists("instalacion", "fecha", "fechaAyer", "fechaManana"));
	}
}