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
import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;

import com.gestion.deportiva.dto.filter.InstalacionHorarioBloqueadoFilter;
import com.gestion.deportiva.exception.PermisoException;

import com.gestion.deportiva.service.InstalacionHorarioBloqueadoService;

import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.InstalacionHorarioBloqueadoUtil;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/instalacion/{idInstalacion}/horario/bloqueado")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
public class PrivadoInstalacionHorarioBloqueadoController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoInstalacionHorarioBloqueadoController.class);

	private static final String BASE_URL = "/privado/instalacion/%s/horario/bloqueado";

	private static final String TITLE_PAGE = "page.title.privado.instalacion.horario.bloqueado";

	private static final String VIEW_FORM = "privado/instalacion/horarioBloqueadoForm";

	private static final String VIEW_LIST = "privado/instalacion/horarioBloqueadoList";

	@Autowired
	private InstalacionHorarioBloqueadoService instalacionHorarioBloqueadoService;

	@GetMapping("")
	public ModelAndView search(@PathVariable Long idInstalacion, Pageable pageable, HttpServletRequest request,
			InstalacionHorarioBloqueadoFilter filter) {
		logger.info("Mostrando vista de listado de instalacionHorarioBloqueado con filtros");
		filter.setInstalacionId(idInstalacion);
		return buildListView(filter, pageable, request);
	}

	@GetMapping("/{id}/editar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
	public ModelAndView editar(@PathVariable Long idInstalacion, @PathVariable Long id,
			RedirectAttributes redirectAttributes) throws PermisoException {
		if (!instalacionHorarioBloqueadoService.canRead(id)) {
			logger.error("Instalacion {} intentó acceder a una instalacionHorarioBloqueado  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), id);
			throw new PermisoException("No tiene permisos para acceder a esta instalacionHorarioBloqueado.");
		}
		return loadForm(id, idInstalacion, redirectAttributes);

	}

	@GetMapping("/crear")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_SEDE + "')")
	public ModelAndView crear(@PathVariable Long idInstalacion, RedirectAttributes redirectAttributes) {

		return loadForm(null, idInstalacion, redirectAttributes);

	}

	@PostMapping("/guardar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
	public ModelAndView guardar(@Valid @ModelAttribute("form") InstalacionHorarioBloqueadoDTO dto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) throws PermisoException {
		if (!instalacionHorarioBloqueadoService.canWrite(dto.getId())) {
			logger.error("Instalacion {} intentó acceder a una instalacionHorarioBloqueado  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta instalacionHorarioBloqueado.");
		}
		logger.info("Guardando datos de la instalacionHorarioBloqueado id: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsForm(dto);
		}
		try {
			instalacionHorarioBloqueadoService.guardar(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(String.format(BASE_URL, dto.getInstalacionId().toString())));
		} catch (Exception e) {
			logger.error("Error al guardar la Instalacion : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	private ModelAndView loadForm(Long id, Long instalacionId, RedirectAttributes redirectAttributes) {
		try {
			InstalacionHorarioBloqueadoDTO dto = instalacionHorarioBloqueadoService.findByIdOrNewEmpty(id, instalacionId);
			return buildDetailsForm(dto);
		} catch (Exception e) {
			logger.error("Error al cargar formulario de Instalacion {}: {}", id, e.getMessage(), e);
			return redirectWithError(BASE_URL, redirectAttributes, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	private ModelAndView buildDetailsForm(InstalacionHorarioBloqueadoDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form", dto);
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.instalacion", String
						.format(BASE_URL, dto.getInstalacionId().toString()).replace("horario/bloqueado", "editar"))
						.add("breadcrumb.gestion.instalacion.horario", null).build());
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildListView(InstalacionHorarioBloqueadoFilter filter, Pageable pageable,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		mav.addObject("page", instalacionHorarioBloqueadoService.getPageByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("url", InstalacionHorarioBloqueadoUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject("breadcrumbs", BreadcrumbBuilder.start().includeHome()
				.add("breadcrumb.gestion.instalacion", String.format(BASE_URL, filter.getInstalacionId().toString())
						.replace("horario/bloqueado", "editar"))
				.add("breadcrumb.gestion.instalacion.horario", null).build());

		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	@GetMapping("/{id}/eliminar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
	public ModelAndView eliminar(@PathVariable Long idInstalacion, @PathVariable Long id,
			RedirectAttributes redirectAttributes) throws PermisoException {
		if (!instalacionHorarioBloqueadoService.canWrite(id)) {
			logger.error("Instalacion {} intentó acceder a una instalacionHorarioBloqueado  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), id);
			throw new PermisoException("No tiene permisos para acceder a esta instalacionHorarioBloqueado.");
		}
		try {
			instalacionHorarioBloqueadoService.eliminar(id);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(String.format(BASE_URL, idInstalacion).toString()));
		} catch (Exception e) {
			logger.error("Error al eliminar instalacionHorarioBloqueado {}", id, e);
			return redirectWithError(BASE_URL, redirectAttributes, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}

	}
}
