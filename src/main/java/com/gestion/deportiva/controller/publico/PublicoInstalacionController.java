package com.gestion.deportiva.controller.publico;

import java.time.LocalDate;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestion.deportiva.controller.BaseController;
import com.gestion.deportiva.dto.InstalacionTipoDTO;
import com.gestion.deportiva.dto.MunicipioDTO;
import com.gestion.deportiva.dto.filter.InstalacionFilter;
import com.gestion.deportiva.dto.filter.InstalacionPublicoFilter;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.service.MunicipioService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.InstalacionUtil;
import com.gestion.deportiva.util.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/publico/instalacion")
public class PublicoInstalacionController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PublicoInstalacionController.class);

	@Autowired
	private InstalacionService instalacionService;
	
	@Autowired
	private InstalacionTipoService instalacionTipoService;
	
	@Autowired
	private MunicipioService municipioService;

	private static final String TITLE_PAGE = "page.title.publico.instalacion";

	private static final String VIEW_FORM = "publico/instalacion/form";
	
	private static final String VIEW_LIST = "publico/instalacion/list";

	private static final String VIEW_DISPONIBILIDAD_FORM = "publico/instalacion/disponibilidadForm";

	@GetMapping("/{id}/detalle")
	public ModelAndView detalle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		return loadForm(id, redirectAttributes);

	}
	
	@GetMapping("/buscar")
	public ModelAndView search(Pageable pageable, HttpServletRequest request, InstalacionPublicoFilter filter) {
		logger.info("Mostrando vista de listado de instalacion con filtros");
		return buildListView(filter, pageable, request);
	}
	
	private ModelAndView buildListView(InstalacionPublicoFilter filter, Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		mav.addObject("listInstalacion",  instalacionService.getListByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("url", InstalacionUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject("listInstalacionTipo", Utils.addEmptyOptionIfMoreThanOneOption(instalacionTipoService.getListDTO(), InstalacionTipoDTO.class));
		mav.addObject("listMunicipio", Utils.addEmptyOptionIfMoreThanOneOption(municipioService.getListDTOConSedes(), MunicipioDTO.class));
		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView loadForm(Long id, RedirectAttributes redirectAttributes) {
		logger.info("Viendo informacion publica de la instalacion id {}", id);
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("instalacion", instalacionService.getPublicoDTOById(id));
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	@GetMapping("/{id}/disponibilidad")
	public ModelAndView mostrarDisponibilidad(@PathVariable("id") Long id,
			@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			RedirectAttributes redirectAttributes) {

		return loadDisponiblidadForm(id, fecha, redirectAttributes);
	}

	private ModelAndView loadDisponiblidadForm(Long id, LocalDate fecha, RedirectAttributes redirectAttributes) {
		logger.info("Viendo informacion la disponibilidad de la instalacion id {}", id);
		ModelAndView mav = new ModelAndView(VIEW_DISPONIBILIDAD_FORM);
		mav.addObject("instalacion", instalacionService.getDisponibilidadDTOById(id, fecha));
		mav.addObject("fecha", fecha);
		mav.addObject("fechaAyer", fecha.minusDays(1));
		mav.addObject("fechaManana", fecha.plusDays(1));
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

}
