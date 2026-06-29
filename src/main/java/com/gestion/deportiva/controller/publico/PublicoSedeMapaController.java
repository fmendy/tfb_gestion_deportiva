package com.gestion.deportiva.controller.publico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gestion.deportiva.controller.BaseController;
import com.gestion.deportiva.dto.filter.SedeMapaFilter;
import com.gestion.deportiva.service.ComunidadAutonomaService;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.service.MunicipioService;
import com.gestion.deportiva.service.ProvinciaService;
import com.gestion.deportiva.service.SedeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/publico/sede/mapa")
public class PublicoSedeMapaController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PublicoSedeMapaController.class);

	@Autowired
	private InstalacionTipoService instalacionTipoService;

	@Autowired
	private SedeService sedeService;

	@Autowired
	private ComunidadAutonomaService comunidadAutonomaService;

	@Autowired
	private ProvinciaService provinciaService;

	@Autowired
	private MunicipioService municipioService;

	private static final String TITLE_PAGE = "page.title.publico.sede.mapa";

	private static final String VIEW_MAP = "publico/sede/mapaList";

	@GetMapping("")
	public ModelAndView search(Pageable pageable, HttpServletRequest request, SedeMapaFilter filter) {
		logger.info("Mostrando mapa de sedes");
		return buildListView(filter, pageable, request);
	}

	private ModelAndView buildListView(SedeMapaFilter filter, Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_MAP);
		mav.addObject("filter", filter);
		mav.addObject("listSedeMapa", sedeService.getListSedeMapaDTO(filter));
		mav.addObject("listInstalacionTipo", instalacionTipoService.getListDTO());
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
