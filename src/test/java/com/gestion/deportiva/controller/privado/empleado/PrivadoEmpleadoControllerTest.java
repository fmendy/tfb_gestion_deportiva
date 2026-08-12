package com.gestion.deportiva.controller.privado.empleado;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.gestion.deportiva.dto.EmpleadoRegistroDTO;
import com.gestion.deportiva.dto.filter.EmpleadoFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.EmpleadoService;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.service.RolService;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class PrivadoEmpleadoControllerTest {

	@Mock
	private EmpleadoService empleadoService;

	@Mock
	private RolService rolService;

	@Mock
	private EmpresaService empresaService;

	@Mock
	private SedeService sedeService;

	@Mock
	private InstalacionService instalacionService;

	@Mock
	private UsuarioService usuarioService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private RedirectAttributes redirectAttributes;

	@Mock
	private BindingResult bindingResult;

	@InjectMocks
	private PrivadoEmpleadoController empleadoController;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		securityUtilMockedStatic.close();
	}

	@Test
	void searchTest() {
		EmpleadoFilter filter = new EmpleadoFilter();
		Pageable pageable = Pageable.unpaged();
		Page<Object> page = new PageImpl<>(List.of());

		when(request.getRequestURI()).thenReturn("/privado/empleado");
		when(empleadoService.getPageByFilter(any(), any())).thenReturn((Page) page);

		ModelAndView mav = empleadoController.search(pageable, request, filter);

		assertThat(mav).isNotNull();
		assertThat(mav.getViewName()).isEqualTo("privado/empleado/list");
		verify(empleadoService).getPageByFilter(any(), any());
	}

	@Test
	void editarPermitidoTest() throws PermisoException {
		Long id = 1L;
		EmpleadoRegistroDTO dto = new EmpleadoRegistroDTO();

		when(empleadoService.canRead(id)).thenReturn(true);
		when(empleadoService.findEmpleadoRegistroById(id)).thenReturn(dto);

		ModelAndView mav = empleadoController.editar(id, redirectAttributes);

		assertThat(mav).isNotNull();
		assertThat(mav.getViewName()).isEqualTo("privado/empleado/form");
		verify(empleadoService).findEmpleadoRegistroById(id);
	}

	@Test
	void editarSinPermisoLanzaExcepcionTest() {
		Long id = 1L;
		when(empleadoService.canRead(id)).thenReturn(false);

		assertThatThrownBy(() -> empleadoController.editar(id, redirectAttributes)).isInstanceOf(PermisoException.class)
				.hasMessage("No tiene permisos para acceder a esta empleado.");
	}

	@Test
	void eliminarExitosoTest() throws PermisoException {
		Long id = 1L;
		when(empleadoService.canRead(id)).thenReturn(true);

		ModelAndView mav = empleadoController.eliminar(id, redirectAttributes);

		assertThat(mav).isNotNull();
		assertThat(mav.getView()).isInstanceOf(RedirectView.class);
		verify(usuarioService).eliminar(id);
	}

	@Test
	void eliminarSinPermisoTest() {
		Long id = 1L;
		when(empleadoService.canRead(id)).thenReturn(false);

		assertThatThrownBy(() -> empleadoController.eliminar(id, redirectAttributes))
				.isInstanceOf(PermisoException.class);
	}

	@Test
	void eliminarLanzaExcepcionInternaTest() throws PermisoException {
		Long id = 1L;
		when(empleadoService.canRead(id)).thenReturn(true);
		doThrow(new RuntimeException("Error DB")).when(usuarioService).eliminar(id);

		ModelAndView mav = empleadoController.eliminar(id, redirectAttributes);

		assertThat(mav).isNotNull();
		verify(redirectAttributes).addFlashAttribute(eq(Constantes.HTTP_STATUS),
				eq(HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	@Test
	void crearTest() {
		ModelAndView mav = empleadoController.crear(redirectAttributes);

		assertThat(mav).isNotNull();
		assertThat(mav.getViewName()).isEqualTo("privado/empleado/registroForm");
	}

	@Test
	void registroGuardarConErroresTest() throws PermisoException {
		EmpleadoRegistroDTO dto = new EmpleadoRegistroDTO();
		dto.setId(1L);

		when(empleadoService.canWrite(1L)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(true);

		ModelAndView mav = empleadoController.registroGuardar(dto, bindingResult, redirectAttributes);

		assertThat(mav).isNotNull();
		assertThat(mav.getViewName()).isEqualTo("privado/empleado/registroForm");
	}

	@Test
	void registroGuardarExitosoTest() throws PermisoException {
		EmpleadoRegistroDTO dto = new EmpleadoRegistroDTO();
		dto.setId(1L);

		when(empleadoService.canWrite(1L)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(empleadoService.guardar(dto)).thenReturn(1L);

		ModelAndView mav = empleadoController.registroGuardar(dto, bindingResult, redirectAttributes);

		assertThat(mav).isNotNull();
		assertThat(mav.getView()).isInstanceOf(RedirectView.class);
		verify(empleadoService).guardar(dto);
	}

	@Test
	void registroGuardarLanzaExcepcionTest() throws PermisoException {
		EmpleadoRegistroDTO dto = new EmpleadoRegistroDTO();
		dto.setId(1L);

		when(empleadoService.canWrite(1L)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(empleadoService.guardar(dto)).thenThrow(new RuntimeException("Error al guardar"));

		ModelAndView mav = empleadoController.registroGuardar(dto, bindingResult, redirectAttributes);

		assertThat(mav).isNotNull();
		assertThat(mav.getViewName()).isEqualTo("privado/empleado/registroForm");
		assertThat(mav.getModel().get(Constantes.HTTP_STATUS)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Test
	void guardarExitosoTest() throws PermisoException {
		EmpleadoRegistroDTO dto = new EmpleadoRegistroDTO();
		dto.setId(1L);

		when(empleadoService.canWrite(1L)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(empleadoService.guardar(dto)).thenReturn(1L);

		ModelAndView mav = empleadoController.guardar(dto, bindingResult, redirectAttributes);

		assertThat(mav).isNotNull();
		assertThat(mav.getView()).isInstanceOf(RedirectView.class);
		verify(empleadoService).guardar(dto);
	}

	@Test
	void guardarConErroresTest() throws PermisoException {
		EmpleadoRegistroDTO dto = new EmpleadoRegistroDTO();
		dto.setId(1L);

		when(empleadoService.canWrite(1L)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(true);

		ModelAndView mav = empleadoController.guardar(dto, bindingResult, redirectAttributes);

		assertThat(mav).isNotNull();
		assertThat(mav.getViewName()).isEqualTo("privado/empleado/form");
	}

	@Test
	void guardarLanzaExcepcionTest() throws PermisoException {
		EmpleadoRegistroDTO dto = new EmpleadoRegistroDTO();
		dto.setId(1L);

		when(empleadoService.canWrite(1L)).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(empleadoService.guardar(dto)).thenThrow(new RuntimeException("Error al guardar"));

		ModelAndView mav = empleadoController.guardar(dto, bindingResult, redirectAttributes);

		assertThat(mav).isNotNull();
		assertThat(mav.getViewName()).isEqualTo("privado/empleado/form");
		assertThat(mav.getModel().get(Constantes.HTTP_STATUS)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}