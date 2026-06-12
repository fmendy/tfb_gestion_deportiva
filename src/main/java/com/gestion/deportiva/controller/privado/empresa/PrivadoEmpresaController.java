package com.gestion.deportiva.controller.privado.empresa;

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
import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.EmpresaUtil;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/empresa")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_EMPRESA + "')")
public class PrivadoEmpresaController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoEmpresaController.class);

	private static final String BASE_URL = "/privado/empresa";

	private static final String TITLE_PAGE = "page.title.privado.empresa";

	private static final String VIEW_LIST = "privado/empresa/list";

	private static final String VIEW_FORM = "privado/empresa/form";

	@Autowired
	private EmpresaService empresaService;

	@GetMapping("")
	public ModelAndView search(Pageable pageable, HttpServletRequest request, EmpresaFilter filter) {
		logger.info("Mostrando vista de listado de libro con filtros");
		return buildListView(filter, pageable, request);
	}

	@GetMapping("/{id}/editar")
	public ModelAndView editar(@PathVariable Long id, RedirectAttributes redirectAttributes) throws PermisoException {
		if (!empresaService.canRead(id)) {
			logger.error("Libro {} intentó acceder a una Empresa  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), id);
			throw new PermisoException("No tiene permisos para acceder a esta empresa.");
		}
		return loadForm(id, redirectAttributes);

	}

	@PostMapping("/guardar")
	public ModelAndView guardar(@Valid @ModelAttribute("form") EmpresaDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws PermisoException {
		if (!empresaService.canWrite(dto.getId())) {
			logger.error("Libro {} intentó acceder a una Empresa  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta empresa.");
		}
		logger.info("Guardando datos de la empresa id: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsForm(dto);
		}
		try {
			Long id = empresaService.guardar(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL + "/" + id + "/editar"));
		} catch (Exception e) {
			logger.error("Error al guardar la libro : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	private ModelAndView loadForm(Long id, RedirectAttributes redirectAttributes) {
		try {
			EmpresaDTO dto = (id == null) ? new EmpresaDTO() : empresaService.findById(id);
			return buildDetailsForm(dto);
		} catch (Exception e) {
			logger.error("Error al cargar formulario de libro {}: {}", id, e.getMessage(), e);
			return redirectWithError(BASE_URL, redirectAttributes, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	private ModelAndView buildDetailsForm(EmpresaDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form", dto);
		mav.addObject("breadcrumbs", BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.empresa", BASE_URL)
				.add("breadcrumb.gestion.empresa.editar", null).build());
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildListView(EmpresaFilter filter, Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		mav.addObject("page", empresaService.getPageByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("url", EmpresaUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.empresa", null).build());
		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}
}
