package com.gestion.deportiva.controller.privado.instalacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.gestion.deportiva.dto.InstalacionHorarioEspecialDTO;

import com.gestion.deportiva.dto.filter.InstalacionHorarioEspecialFilter;
import com.gestion.deportiva.exception.PermisoException;

import com.gestion.deportiva.service.InstalacionHorarioEspecialService;

import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.InstalacionHorarioEspecialUtil;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/instalacion/{idInstalacion}/horario/especial")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
public class PrivadoInstalacionHorarioEspecialController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoInstalacionHorarioEspecialController.class);

	private static final String BASE_URL = "/privado/instalacion/%s/horario/especial";

	private static final String TITLE_PAGE = "page.title.privado.instalacion.horario.especial";

	private static final String VIEW_FORM = "privado/instalacion/horarioEspecialForm";

	private static final String VIEW_LIST = "privado/instalacion/horarioEspecialList";

	@Autowired
	private InstalacionHorarioEspecialService instalacionHorarioEspecialService;

	@GetMapping("")
	public ModelAndView search(@PathVariable Long idInstalacion,Pageable pageable, HttpServletRequest request, InstalacionHorarioEspecialFilter filter) {
		logger.info("Mostrando vista de listado de instalacionHorarioEspecial con filtros");
		filter.setInstalacionId(idInstalacion);
		return buildListView(filter, pageable, request);
	}

	@GetMapping("/{id}/editar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
	public ModelAndView editar(@PathVariable Long idInstalacion, @PathVariable Long id,
			RedirectAttributes redirectAttributes) throws PermisoException {
		if (!instalacionHorarioEspecialService.canRead(id)) {
			logger.error("Instalacion {} intentó acceder a una instalacionHorarioEspecial  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), id);
			throw new PermisoException("No tiene permisos para acceder a esta instalacionHorarioEspecial.");
		}
		return loadForm(id, redirectAttributes);

	}

	@GetMapping("/crear")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_SEDE + "')")
	public ModelAndView crear(@PathVariable Long idInstalacion, RedirectAttributes redirectAttributes) {

		return loadForm(null, redirectAttributes);

	}

	@PostMapping("/guardar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
	public ModelAndView guardar(@Valid @ModelAttribute("form") InstalacionHorarioEspecialDTO dto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) throws PermisoException {
		if (!instalacionHorarioEspecialService.canWrite(dto.getId())) {
			logger.error("Instalacion {} intentó acceder a una instalacionHorarioEspecial  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta instalacionHorarioEspecial.");
		}
		logger.info("Guardando datos de la instalacionHorarioEspecial id: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsForm(dto);
		}
		try {
			Long id = instalacionHorarioEspecialService.guardar(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL + "/" + id + "/editar"));
		} catch (Exception e) {
			logger.error("Error al guardar la Instalacion : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	private ModelAndView loadForm(Long id, RedirectAttributes redirectAttributes) {
		try {
			InstalacionHorarioEspecialDTO dto = (id == null) ? new InstalacionHorarioEspecialDTO()
					: instalacionHorarioEspecialService.findById(id);
			return buildDetailsForm(dto);
		} catch (Exception e) {
			logger.error("Error al cargar formulario de Instalacion {}: {}", id, e.getMessage(), e);
			return redirectWithError(BASE_URL, redirectAttributes, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	private ModelAndView buildDetailsForm(InstalacionHorarioEspecialDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form", dto);
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.instalacion", BASE_URL)
						.add("breadcrumb.gestion.instalacion.editar", null).build());
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildListView(InstalacionHorarioEspecialFilter filter, Pageable pageable,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		mav.addObject("page", instalacionHorarioEspecialService.getPageByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("url", InstalacionHorarioEspecialUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome()
						.add("breadcrumb.gestion.instalacion",
								String.format(BASE_URL, filter.getInstalacionId().toString()).replace("horario/especial", "editar"))
						.add("breadcrumb.gestion.instalacion.horario", null).build());

		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}
}
