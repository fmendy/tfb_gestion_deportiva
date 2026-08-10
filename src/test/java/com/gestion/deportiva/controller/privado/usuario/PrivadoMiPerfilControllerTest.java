package com.gestion.deportiva.controller.privado.usuario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.service.PdfReportService;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class PrivadoMiPerfilControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UsuarioService usuarioService;

	@Mock
	private PdfReportService pdfReportService;

	@Mock
	private UserDetails userDetails;

	@InjectMocks
	private PrivadoMiPerfilController privadoMiPerfilController;

	private static final String BASE_URL = "/privado/usuario/miperfil";

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
		validatorFactory.afterPropertiesSet();

		mockMvc = MockMvcBuilders.standaloneSetup(privadoMiPerfilController).setValidator(validatorFactory).build();

		securityUtilMockedStatic = Mockito.mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		if (securityUtilMockedStatic != null) {
			securityUtilMockedStatic.close();
		}
	}

	@Test
	void guardarPerfilExitoTest() throws Exception {
		MiPerfilDTO perfilDTO = new MiPerfilDTO();
		perfilDTO.setId(1L);
		perfilDTO.setNombre("Nombre de prueba");
		perfilDTO.setEmail("correo@prueba.com");
		perfilDTO.setEmailConfirmar("correo@prueba.com");

		mockMvc.perform(post(BASE_URL + "/guardar").flashAttr("form", perfilDTO)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(BASE_URL))
				.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));

		verify(usuarioService).actualizarMiPerfil(any(MiPerfilDTO.class));
	}

	@Test
	void guardarPerfilConErroresValidacionTest() throws Exception {
		mockMvc.perform(post(BASE_URL + "/guardar").flashAttr("form", new MiPerfilDTO())).andExpect(status().isOk())
				.andExpect(view().name("usuario/miperfil/form"));
	}

	@Test
	void descargarInformeArcoTest() throws Exception {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

		mockMvc.perform(get(BASE_URL + "/descargar-datos-arco")).andExpect(status().isOk());

		verify(pdfReportService).exportarDatosArcoUsuarioPdf(eq(1L), any(HttpServletResponse.class));
	}

	@Test
	void descargarInformeArcoExcepcionTest() throws Exception {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
		doThrow(new java.io.IOException("Error I/O")).when(pdfReportService).exportarDatosArcoUsuarioPdf(eq(1L),
				any(HttpServletResponse.class));

		mockMvc.perform(get(BASE_URL + "/descargar-datos-arco")).andExpect(status().isInternalServerError());
	}

	@Test
	void eliminarCuentaTest() throws Exception {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

		mockMvc.perform(get(BASE_URL + "/eliminar")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?logout=true"))
				.andExpect(flash().attribute(Constantes.HTTP_STATUS, HttpStatus.OK.value()));

		verify(usuarioService).borrarMiCuenta();
	}

}