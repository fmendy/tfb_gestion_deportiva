package com.gestion.deportiva.controller.publico;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gestion.deportiva.dto.UsuarioPasswordDTO;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.Constantes;

@ExtendWith(MockitoExtension.class)
class PublicoPasswordControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UsuarioService usuarioService;

	@Mock
	private Validator validator;

	@InjectMocks
	private PublicoPasswordController publicoPasswordController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(publicoPasswordController).setViewResolvers(viewResolver)
				.setValidator(validator).build();
	}

	@Test
	void shouldResetForm() throws Exception {
		mockMvc.perform(get("/publico/password")).andExpect(status().isOk())
				.andExpect(view().name("publico/password/form")).andExpect(model().attributeExists("form"));
	}

	@Test
	void shouldResetPostSuccess() {
		try {
			mockMvc.perform(post("/publico/password/reset").param("email", "test@test.com"))
					.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/publico/password"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(usuarioService, times(1)).enviarMailPasswordOlvidada(any(UsuarioPasswordDTO.class));
	}

	@Test
	void shouldResetPostThrowsExceptionHandling() throws Exception {
		doThrow(new RuntimeException("Mail error")).when(usuarioService)
				.enviarMailPasswordOlvidada(any(UsuarioPasswordDTO.class));

		mockMvc.perform(post("/publico/password/reset").param("email", "test@test.com")).andExpect(status().isOk())
				.andExpect(view().name("publico/password/form"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	@Test
	void shouldResetearSuccess() {
		try {
			mockMvc.perform(get("/publico/password/resetear").param("email", "test@test.com"))
					.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/publico/password"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(usuarioService, times(1)).generarPasswordYEnviarMail(any(UsuarioPasswordDTO.class));
	}

	@Test
	void shouldResetearThrowsExceptionHandling() throws Exception {
		doThrow(new RuntimeException("Reset error")).when(usuarioService)
				.generarPasswordYEnviarMail(any(UsuarioPasswordDTO.class));

		mockMvc.perform(get("/publico/password/resetear").param("email", "test@test.com")).andExpect(status().isOk())
				.andExpect(view().name("publico/password/form"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
}