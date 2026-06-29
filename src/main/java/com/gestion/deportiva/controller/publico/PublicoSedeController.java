package com.gestion.deportiva.controller.publico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestion.deportiva.controller.BaseController;
import com.gestion.deportiva.dto.filter.SedePublicoFilter;
import com.gestion.deportiva.service.ComunidadAutonomaService;
import com.gestion.deportiva.service.MunicipioService;
import com.gestion.deportiva.service.ProvinciaService;
import com.gestion.deportiva.service.SedeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/publico/sede")
public class PublicoSedeController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PublicoSedeController.class);

	@Autowired
	private SedeService sedeService;

	@Autowired
	private ComunidadAutonomaService comunidadAutonomaService;

	@Autowired
	private ProvinciaService provinciaService;

	@Autowired
	private MunicipioService municipioService;

	private static final String TITLE_PAGE = "page.title.publico.sede";

	private static final String TITLE_PAGE_MAP = "page.title.publico.sede.mapa";

	private static final String VIEW_MAP = "publico/sede/mapaList";

	private static final String VIEW_FORM = "publico/sede/form";

	@GetMapping("/mapa")
	public ModelAndView mapa(Pageable pageable, HttpServletRequest request, SedePublicoFilter filter) {
		logger.info("Mostrando mapa de sedes");
		return buildMapView(filter, pageable, request);
	}

	@GetMapping("/{id}/detalle")
	public ModelAndView detalle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		return loadForm(id, redirectAttributes);

	}

	private ModelAndView loadForm(Long id, RedirectAttributes redirectAttributes) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("sede", sedeService.getSedePublicoDTOById(id));
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildMapView(SedePublicoFilter filter, Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_MAP);
		mav.addObject("filter", filter);
		mav.addObject("listSedeMapa", sedeService.getListSedePublicoDTO(filter));
		mav.addObject("listComunidades", comunidadAutonomaService.getListDTO());
		mav.addObject("listProvincias",
				provinciaService.getListDTOByComunidadAutonomaId(filter.getComunidadAutonomaId()));
		mav.addObject("listMunicipios", municipioService.getListDTOByComunidadAutonomaIdOrProvinciaId(
				filter.getComunidadAutonomaId(), filter.getProvinciaId()));
		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE_MAP, false);
		return mav;
	}

}
