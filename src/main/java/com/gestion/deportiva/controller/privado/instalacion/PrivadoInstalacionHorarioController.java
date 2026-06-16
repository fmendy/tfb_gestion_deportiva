package com.gestion.deportiva.controller.privado.instalacion;

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
import com.gestion.deportiva.dto.InstalacionHorarioSemanalDTO;
import com.gestion.deportiva.exception.PermisoException;

import com.gestion.deportiva.service.InstalacionHorarioService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;

import com.gestion.deportiva.util.SecurityUtil;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/instalacion/{idInstalacion}/horario")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
public class PrivadoInstalacionHorarioController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoInstalacionHorarioController.class);

	private static final String BASE_URL = "/privado/instalacion/%s/horario";

	private static final String TITLE_PAGE = "page.title.privado.instalacion.horario";

	private static final String VIEW_FORM = "privado/instalacion/horarioForm";

	@Autowired
	private InstalacionHorarioService instalacionHorarioService;

	@Autowired
	private InstalacionService instalacionService;

	@GetMapping("")
	public ModelAndView ver(@PathVariable Long idInstalacion, RedirectAttributes redirectAttributes) {
		logger.info("Mostrando vista de listado de instalacion con filtros");
		return buildDetailsForm(instalacionHorarioService.cargarHorarioSemanal(idInstalacion));
	}

	@PostMapping("/guardar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
	public ModelAndView guardar(@Valid @ModelAttribute("form") InstalacionHorarioSemanalDTO dto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) throws PermisoException {
		if (!instalacionService.canWrite(dto.getInstalacionId())) {
			logger.error("Instalacion {} intentó acceder a una Instalacion  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta instalacion.");
		}
		logger.info("Guardando datos de la instalacion id: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsForm(dto);
		}
		try {
			instalacionHorarioService.guardar(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(String.format(BASE_URL, dto.getInstalacionId().toString())));
		} catch (Exception e) {
			logger.error("Error al guardar la Instalacion : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	private ModelAndView buildDetailsForm(InstalacionHorarioSemanalDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form", dto);
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.instalacion.horario", null).build());

		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.instalacion.horario", BASE_URL)
						.add("breadcrumb.gestion.instalacion.editar", null).build());
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

}
