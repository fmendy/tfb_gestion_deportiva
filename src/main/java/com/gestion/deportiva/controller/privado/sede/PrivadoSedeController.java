package com.gestion.deportiva.controller.privado.sede;

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
import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.dto.filter.SedeFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.ComunidadAutonomaService;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.MunicipioService;
import com.gestion.deportiva.service.ProvinciaService;
import com.gestion.deportiva.service.SedeService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SedeUtil;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/sede")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_SEDE + "')")
public class PrivadoSedeController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoSedeController.class);

	private static final String BASE_URL = "/privado/sede";

	private static final String TITLE_PAGE = "page.title.privado.sede";

	private static final String VIEW_LIST = "privado/sede/list";

	private static final String VIEW_FORM = "privado/sede/form";

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private SedeService sedeService;

	@Autowired
	private ComunidadAutonomaService comunidadAutonomaService;

	@Autowired
	private ProvinciaService provinciaService;

	@Autowired
	private MunicipioService municipioService;

	@GetMapping("")
	public ModelAndView search(Pageable pageable, HttpServletRequest request, SedeFilter filter) {
		logger.info("Mostrando vista de listado de sede con filtros");
		return buildListView(filter, pageable, request);
	}

	@GetMapping("/{id}/editar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_SEDE + "')")
	public ModelAndView editar(@PathVariable Long id, RedirectAttributes redirectAttributes) throws PermisoException {
		if (!sedeService.canRead(id)) {
			logger.error("Sede {} intentó acceder a una Sede  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), id);
			throw new PermisoException("No tiene permisos para acceder a esta sede.");
		}
		return loadForm(id, redirectAttributes);

	}

	@GetMapping("/crear")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_EMPRESA + "')")
	public ModelAndView crear(RedirectAttributes redirectAttributes) {

		return loadForm(null, redirectAttributes);

	}

	@PostMapping("/guardar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_SEDE + "')")
	public ModelAndView guardar(@Valid @ModelAttribute("form") SedeDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws PermisoException {
		if (!sedeService.canWrite(dto.getId())) {
			logger.error("Sede {} intentó acceder a una Sede  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta sede.");
		}
		logger.info("Guardando datos de la sede id: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsForm(dto);
		}
		try {
			Long id = sedeService.guardar(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL + "/" + id + "/editar"));
		} catch (Exception e) {
			logger.error("Error al guardar la Sede : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	private ModelAndView loadForm(Long id, RedirectAttributes redirectAttributes) {
		try {
			SedeDTO dto = (id == null) ? new SedeDTO() : sedeService.findById(id);
			return buildDetailsForm(dto);
		} catch (Exception e) {
			logger.error("Error al cargar formulario de Sede {}: {}", id, e.getMessage(), e);
			return redirectWithError(BASE_URL, redirectAttributes, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	private ModelAndView buildDetailsForm(SedeDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form", dto);
		mav.addObject("listEmpresas", empresaService.getListDTO(new EmpresaFilter()));
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.sede", null).build());
		mav.addObject("listComunidades", comunidadAutonomaService.getListDTO());
		mav.addObject("listProvincias", provinciaService.getListDTOByComunidadAutonomaId(dto.getComunidadAutonomaId()));
		mav.addObject("listMunicipios", municipioService
				.getListDTOByComunidadAutonomaIdOrProvinciaId(dto.getComunidadAutonomaId(), dto.getProvinciaId()));
		mav.addObject("breadcrumbs", BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.sede", BASE_URL)
				.add("breadcrumb.gestion.sede.editar", null).build());
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildListView(SedeFilter filter, Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		mav.addObject("page", sedeService.getPageByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("listEmpresas", empresaService.getListDTO(new EmpresaFilter()));
		mav.addObject("url", SedeUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.sede", null).build());
		mav.addObject("listComunidades", comunidadAutonomaService.getListDTO());
		mav.addObject("listProvincias",
				provinciaService.getListDTOByComunidadAutonomaId(filter.getComunidadAutonomaId()));
		mav.addObject("listMunicipios", municipioService.getListDTOByComunidadAutonomaIdOrProvinciaId(
				filter.getComunidadAutonomaId(), filter.getProvinciaId()));

		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}
}
