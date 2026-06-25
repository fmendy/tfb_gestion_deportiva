package com.gestion.deportiva.controller.privado.empleado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.gestion.deportiva.dto.EmpleadoRolDTO;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.EmpleadoService;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.service.RolService;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/empleado/{idEmpleado}/rol")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Rol.GESTION_ROL + "')")
public class PrivadoEmpleadoRolController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoEmpleadoController.class);

	private static final String BASE_URL = "/privado/empleado/%s/rol";

	private static final String TITLE_PAGE = "page.title.privado.empleado.rol";

	private static final String VIEW_FORM = "privado/empleado/rolForm";

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private SedeService sedeService;

	@Autowired
	private InstalacionService instalacionService;

	@Autowired
	private RolService rolService;

	@GetMapping("")
	public ModelAndView ver(@PathVariable Long idEmpleado, RedirectAttributes redirectAttributes) {
		logger.info("Mostrando rol para empleado");
		return buildDetailsForm(empleadoService.getEmpleadoRolDTO(idEmpleado));
	}

	@PostMapping("/guardar")
	public ModelAndView guardar(@Valid @ModelAttribute("form") EmpleadoRolDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws PermisoException {
		if (!empleadoService.canWrite(dto.getId())) {
			logger.error("Empleado {} intentó acceder a una Empleado  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta empleado.");
		}

		if (bindingResult.hasErrors()) {
			return buildDetailsForm(dto);
		}
		logger.info("Guardando datos de la empleado id: {}", dto.getId());
		try {
			empleadoService.guardarEmpleadoRol(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(String.format(BASE_URL, dto.getUsuarioId().toString())));
		} catch (Exception e) {
			logger.error("Error al guardar la Empleado : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	private ModelAndView buildDetailsForm(EmpleadoRolDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form", dto);
		mav.addObject("listRoles", rolService.getListDTOParaEmpleado());
		mav.addObject("listEmpresas", empresaService.getListDTOParaEmpleado());
		mav.addObject("listSedes", sedeService.getListDTOParaEmpleado(null));
		mav.addObject("listInstalaciones", instalacionService.getListDTOParaEmpleado(null, null));
		mav.addObject("breadcrumbs", BreadcrumbBuilder.start().includeHome()
				.add("breadcrumb.gestion.empleado", BASE_URL).add("breadcrumb.gestion.empleado.editar", null).build());
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

}
