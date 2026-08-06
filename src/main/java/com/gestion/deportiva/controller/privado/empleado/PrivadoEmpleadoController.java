package com.gestion.deportiva.controller.privado.empleado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.gestion.deportiva.controller.BaseController;
import com.gestion.deportiva.dto.EmpleadoRegistroDTO;
import com.gestion.deportiva.dto.filter.EmpleadoFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.EmpleadoService;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.service.RolService;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.EmpleadoUtil;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/empleado")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION + "')")
public class PrivadoEmpleadoController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoEmpleadoController.class);

	private static final String BASE_URL = "/privado/empleado";

	private static final String TITLE_PAGE = "page.title.privado.empleado";

	private static final String VIEW_LIST = "privado/empleado/list";

	private static final String VIEW_FORM = "privado/empleado/form";

	private static final String VIEW_REGISTRO_FORM = "privado/empleado/registroForm";

	private static final String MSG_EMPLEADO_PERMISOS = "Empleado {} intento acceder a una Empleado  sin permisos: usuario {}";

	private final EmpleadoService empleadoService;

	private final RolService rolService;

	private final EmpresaService empresaService;

	private final SedeService sedeService;

	private final InstalacionService instalacionService;

	private final UsuarioService usuarioService;

	PrivadoEmpleadoController(EmpleadoService empleadoService, RolService rolService, EmpresaService empresaService,
			SedeService sedeService, InstalacionService instalacionService, UsuarioService usuarioService) {
		this.empleadoService = empleadoService;
		this.rolService = rolService;
		this.empresaService = empresaService;
		this.sedeService = sedeService;
		this.instalacionService = instalacionService;
		this.usuarioService = usuarioService;
	}

	@GetMapping("")
	public ModelAndView search(Pageable pageable, HttpServletRequest request, EmpleadoFilter filter) {
		logger.info("Mostrando vista de listado de Empleado con filtros");
		return buildListView(filter, pageable, request);
	}

	@GetMapping("/{id}/editar")
	public ModelAndView editar(@PathVariable Long id, RedirectAttributes redirectAttributes) throws PermisoException {
		if (!empleadoService.canRead(id)) {
			logger.error(MSG_EMPLEADO_PERMISOS, SecurityUtil.getCurrentUserId(), id);
			throw new PermisoException("No tiene permisos para acceder a esta empleado.");
		}
		return loadForm(id, redirectAttributes);

	}

	@GetMapping("/{id}/eliminar")
	public ModelAndView eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) throws PermisoException {
		if (!empleadoService.canRead(id)) {
			logger.error(MSG_EMPLEADO_PERMISOS, SecurityUtil.getCurrentUserId(), id);
			throw new PermisoException("No tiene permisos para acceder a este empleado.");
		}
		try {
			usuarioService.eliminar(id);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL));
		} catch (Exception e) {
			logger.error("Se ha producido un error al eliminar el empleado con id {}", id, e);
			return redirectWithError(BASE_URL, redirectAttributes, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}

	}

	@GetMapping("/crear")
	public ModelAndView crear(RedirectAttributes redirectAttributes) {

		return buildDetailsRegistroForm(new EmpleadoRegistroDTO());

	}

	@PostMapping("/registro/guardar")
	public ModelAndView registroGuardar(@Valid @ModelAttribute("form") EmpleadoRegistroDTO dto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) throws PermisoException {
		if (!empleadoService.canWrite(dto.getId())) {
			logger.error(MSG_EMPLEADO_PERMISOS, SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta empleado.");
		}

		if (bindingResult.hasErrors()) {
			return buildDetailsRegistroForm(dto);
		}
		logger.info("Guardando registro datos de la empleado id: {}", dto.getId());
		try {
			Long id = empleadoService.guardar(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL + "/" + id + "/editar"));
		} catch (Exception e) {
			logger.error("Error al guardar la Empleado : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsRegistroForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	@PostMapping("/guardar")
	public ModelAndView guardar(@Valid @ModelAttribute("form") EmpleadoRegistroDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws PermisoException {
		if (!empleadoService.canWrite(dto.getId())) {
			logger.error(MSG_EMPLEADO_PERMISOS, SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta empleado.");
		}

		if (bindingResult.hasErrors()) {
			return buildDetailsForm(dto);
		}
		logger.info("Guardando datos de la empleado id: {}", dto.getId());
		try {
			Long id = empleadoService.guardar(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL + "/" + id + "/editar"));
		} catch (Exception e) {
			logger.error("Error al guardar la Empleado : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	private ModelAndView loadForm(Long id, RedirectAttributes redirectAttributes) {
		try {
			EmpleadoRegistroDTO dto = (id == null) ? new EmpleadoRegistroDTO()
					: empleadoService.findEmpleadoRegistroById(id);
			return buildDetailsForm(dto);
		} catch (Exception e) {
			logger.error("Error al cargar formulario de Empleado {}: {}", id, e.getMessage(), e);
			return redirectWithError(BASE_URL, redirectAttributes, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	private ModelAndView buildDetailsForm(EmpleadoRegistroDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form", dto);
		mav.addObject(Constantes.Breadcrumbs.BREADCRUMB,
				BreadcrumbBuilder.start().includeHome().add(Constantes.Breadcrumbs.GESTION_EMPLEADO, BASE_URL)
						.add("breadcrumb.gestion.empleado.editar", null).build());
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildDetailsRegistroForm(EmpleadoRegistroDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_REGISTRO_FORM);
		mav.addObject("form", dto);
		mav.addObject("listRoles", rolService.getListDTOParaEmpleado());
		mav.addObject("listEmpresas", empresaService.getListDTOParaEmpleado());
		mav.addObject("listSedes", sedeService.getListDTOParaEmpleado(null));
		mav.addObject("listInstalaciones", instalacionService.getListDTOParaEmpleado(null, null));
		mav.addObject(Constantes.Breadcrumbs.BREADCRUMB,
				BreadcrumbBuilder.start().includeHome().add(Constantes.Breadcrumbs.GESTION_EMPLEADO, BASE_URL)
						.add("breadcrumb.gestion.empleado.registro", null).build());
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildListView(EmpleadoFilter filter, Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		mav.addObject("page", empleadoService.getPageByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("listEmpresas", empresaService.getListDTOParaEmpleado());
		mav.addObject("listSedes", sedeService.getListDTOParaEmpleado(filter.getEmpresaId()));
		mav.addObject("listInstalaciones",
				instalacionService.getListDTOParaEmpleado(filter.getEmpresaId(), filter.getSedeId()));
		mav.addObject("url", EmpleadoUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject(Constantes.Breadcrumbs.BREADCRUMB,
				BreadcrumbBuilder.start().includeHome().add(Constantes.Breadcrumbs.GESTION_EMPLEADO, null).build());
		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}
}
