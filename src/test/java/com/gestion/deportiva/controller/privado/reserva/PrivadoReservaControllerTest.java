package com.gestion.deportiva.controller.privado.reserva;

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

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.service.ReservaEstadoService;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.SecurityUtil;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class PrivadoReservaControllerTest {

	private MockMvc mockMvc;

	@Mock
	private ReservaService reservaService;

	@Mock
	private InstalacionTipoService instalacionTipoService;

	@Mock
	private ReservaEstadoService reservaEstadoService;

	@InjectMocks
	private PrivadoReservaController privadoReservaController;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

		mockMvc = MockMvcBuilders.standaloneSetup(privadoReservaController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).setValidator(validator)
				.build();

		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		securityUtilMockedStatic.close();
	}

	@Test
	void solicitudTest() throws Exception {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
		when(reservaService.getFullReservaSolicitudDTOByReservaSolictudDTO(any()))
				.thenReturn(new ReservaSolicitudDTO());

		mockMvc.perform(get("/privado/reserva/solicitud").param("instalacionId", "1")).andExpect(status().isOk())
				.andExpect(view().name("privado/reserva/solicitudForm")).andExpect(model().attributeExists("form"));
	}

	@Test
	void misReservasTest() throws Exception {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
		when(reservaService.getReservaFilterParaMisReservas()).thenReturn(new ReservaFilter());
		when(reservaService.getPageMiReservaListadoDTOByFilter(any(), any())).thenReturn(new PageImpl<>(List.of()));

		mockMvc.perform(get("/privado/reserva/misreservas")).andExpect(status().isOk())
				.andExpect(view().name("privado/reserva/misReservasList")).andExpect(model().attributeExists("page"));
	}

	@Test
	void misReservasPasadasTest() throws Exception {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
		when(reservaService.getReservaFilterParaMisReservasPasadas()).thenReturn(new ReservaFilter());
		when(reservaService.getPageMiReservaListadoDTOByFilter(any(), any())).thenReturn(new PageImpl<>(List.of()));

		mockMvc.perform(get("/privado/reserva/misreservas/pasadas")).andExpect(status().isOk())
				.andExpect(view().name("privado/reserva/misReservasList"))
				.andExpect(model().attribute("reservasPasadas", true));
	}

	@Test
	void verHistoricoTest() throws Exception {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
		when(reservaService.getListHistorico(1L)).thenReturn(List.of());

		mockMvc.perform(get("/privado/reserva/1/historico")).andExpect(status().isOk())
				.andExpect(view().name("privado/reserva/historicoList"))
				.andExpect(model().attributeExists("historico"));
	}

	@Test
	void cancelarUsuarioExitosoTest() throws Exception {
		when(reservaService.canCancelarUsuario(1L)).thenReturn(true);

		mockMvc.perform(get("/privado/reserva/1/cancelarusuario")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/privado/reserva/misreservas"));

		verify(reservaService).cancelarUsuario(1L);
	}

	@Test
	void aprobarExitosoTest() throws Exception {
		when(reservaService.canAprobarDenegarReserva(1L)).thenReturn(true);

		mockMvc.perform(get("/privado/reserva/1/aprobar")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/privado/reserva"));

		verify(reservaService).aprobar(1L);
	}

	@Test
	void searchTest() throws Exception {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
		when(reservaService.getPageListadoByFilter(any(), any())).thenReturn(new PageImpl<>(List.of()));
		when(instalacionTipoService.getListDTO()).thenReturn(List.of());
		when(reservaEstadoService.getListDTO()).thenReturn(List.of());

		mockMvc.perform(get("/privado/reserva")).andExpect(status().isOk())
				.andExpect(view().name("privado/reserva/list")).andExpect(model().attributeExists("page"));
	}
}