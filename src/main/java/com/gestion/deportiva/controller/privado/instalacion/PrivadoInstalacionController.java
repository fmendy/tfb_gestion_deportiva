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
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.InstalacionTipoDTO;
import com.gestion.deportiva.dto.filter.InstalacionFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.InstalacionUtil;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/instalacion")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
public class PrivadoInstalacionController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoInstalacionController.class);

	private static final String BASE_URL = "/privado/instalacion";

	private static final String TITLE_PAGE = "page.title.privado.instalacion";

	private static final String VIEW_LIST = "privado/instalacion/list";

	private static final String VIEW_FORM = "privado/instalacion/form";

	@Autowired
	private InstalacionService instalacionService;

	@Autowired
	private SedeService sedeService;

	@Autowired
	private InstalacionTipoService instalacionTipoService;

	@Autowired
	private EmpresaService empresaService;

	@GetMapping("")
	public ModelAndView search(Pageable pageable, HttpServletRequest request, InstalacionFilter filter) {
		logger.info("Mostrando vista de listado de instalacion con filtros");
		return buildListView(filter, pageable, request);
	}

	@GetMapping("/{id}/editar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
	public ModelAndView editar(@PathVariable Long id, RedirectAttributes redirectAttributes) throws PermisoException {
		if (!instalacionService.canRead(id)) {
			logger.error("Instalacion {} intentó acceder a una Instalacion  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), id);
			throw new PermisoException("No tiene permisos para acceder a esta instalacion.");
		}
		return loadForm(id, redirectAttributes);

	}

	@GetMapping("/crear")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_SEDE + "')")
	public ModelAndView crear(RedirectAttributes redirectAttributes) {

		return loadForm(null, redirectAttributes);

	}

	@PostMapping("/guardar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_INSTALACION + "')")
	public ModelAndView guardar(@Valid @ModelAttribute("form") InstalacionDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws PermisoException {
		if (!instalacionService.canWrite(dto.getId())) {
			logger.error("Instalacion {} intentó acceder a una Instalacion  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta instalacion.");
		}
		logger.info("Guardando datos de la instalacion id: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsForm(dto);
		}
		try {
			Long id = instalacionService.guardar(dto);
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
			InstalacionDTO dto = (id == null) ? new InstalacionDTO() : instalacionService.findById(id);
			return buildDetailsForm(dto);
		} catch (Exception e) {
			logger.error("Error al cargar formulario de Instalacion {}: {}", id, e.getMessage(), e);
			return redirectWithError(BASE_URL, redirectAttributes, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	private ModelAndView buildDetailsForm(InstalacionDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form", dto);
		mav.addObject("listEmpresas", empresaService.getListDTOParaInstalacion());
		mav.addObject("listSedes", sedeService.getListDTOParaInstalacion(dto.getEmpresaId()));
		mav.addObject("listInstalacionTipo", Utils.addEmptyOptionIfMoreThanOneOption(instalacionTipoService.getListDTO(), InstalacionTipoDTO.class));
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.instalacion", null).build());

		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.instalacion", BASE_URL)
						.add("breadcrumb.gestion.instalacion.editar", null).build());
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildListView(InstalacionFilter filter, Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		mav.addObject("page", instalacionService.getPageByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("url", InstalacionUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.instalacion", null).build());
		mav.addObject("listEmpresas", empresaService.getListDTOParaInstalacion());
		mav.addObject("listInstalacionTipo", Utils.addEmptyOptionIfMoreThanOneOption(instalacionTipoService.getListDTO(), InstalacionTipoDTO.class));
		mav.addObject("listSedes", sedeService.getListDTOParaInstalacion(filter.getEmpresaId()));
		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}
}
