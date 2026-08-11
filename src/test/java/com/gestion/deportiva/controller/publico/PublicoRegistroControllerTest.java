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

import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.service.RegistroEmpresaService;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.Constantes;

@ExtendWith(MockitoExtension.class)
class PublicoRegistroControllerTest {

	private MockMvc mockMvc;

	@Mock
	private RegistroEmpresaService registroEmpresaService;

	@Mock
	private UsuarioService usuarioService;

	@Mock
	private Validator validator;

	@InjectMocks
	private PublicoRegistroController publicoRegistroController;

	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(publicoRegistroController).setViewResolvers(viewResolver)
				.setValidator(validator).build();
	}

	@Test
	void shouldGetUsuarioForm() throws Exception {
		mockMvc.perform(get("/publico/registro/usuario")).andExpect(status().isOk())
				.andExpect(view().name("publico/registro/usuarioForm")).andExpect(model().attributeExists("form"));
	}

	@Test
	void shouldGuardarUsuarioSuccess() {
		try {
			mockMvc.perform(post("/publico/registro/usuario/guardar").param("email", "test@test.com")
					.param("password", "password123").param("nombre", "Test User"))
					.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(usuarioService, times(1)).registrarUsuarioCliente(any(UsuarioRegistroDTO.class));
	}

	@Test
	void shouldGuardarUsuarioThrowsExceptionHandling() throws Exception {
		doThrow(new RuntimeException("Registration error")).when(usuarioService)
				.registrarUsuarioCliente(any(UsuarioRegistroDTO.class));

		mockMvc.perform(post("/publico/registro/usuario/guardar").param("email", "test@test.com")
				.param("password", "password123").param("nombre", "Test User")).andExpect(status().isOk())
				.andExpect(view().name("publico/registro/usuarioForm"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	@Test
	void shouldGetEmpresaForm() throws Exception {
		mockMvc.perform(get("/publico/registro/empresa")).andExpect(status().isOk())
				.andExpect(view().name("publico/registro/empresaForm")).andExpect(model().attributeExists("form"));
	}

	@Test
	void shouldGuardarEmpresaSuccess() {
		try {
			mockMvc.perform(
					post("/publico/registro/empresa/guardar").param("nombre", "Empresa Test").param("cif", "B12345678"))
					.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"))
					.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		verify(registroEmpresaService, times(1)).registrarEmpresa(any(EmpresaRegistroDTO.class));
	}

	@Test
	void shouldGuardarEmpresaThrowsExceptionHandling() throws Exception {
		doThrow(new RuntimeException("Database error")).when(registroEmpresaService)
				.registrarEmpresa(any(EmpresaRegistroDTO.class));

		mockMvc.perform(
				post("/publico/registro/empresa/guardar").param("nombre", "Empresa Test").param("cif", "B12345678"))
				.andExpect(status().isOk()).andExpect(view().name("publico/registro/empresaForm"))
				.andExpect(model().attribute(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
}