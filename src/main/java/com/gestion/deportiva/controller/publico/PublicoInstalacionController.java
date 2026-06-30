package com.gestion.deportiva.controller.publico;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestion.deportiva.controller.BaseController;

import com.gestion.deportiva.service.InstalacionService;

@Controller
@RequestMapping(value = "/publico/instalacion")
public class PublicoInstalacionController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PublicoInstalacionController.class);

	@Autowired
	private InstalacionService instalacionService;

	private static final String TITLE_PAGE = "page.title.publico.instalacion";

	private static final String VIEW_FORM = "publico/instalacion/form";

	private static final String VIEW_DISPONIBILIDAD_FORM = "publico/instalacion/disponibilidadForm";

	@GetMapping("/{id}/detalle")
	public ModelAndView detalle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		return loadForm(id, redirectAttributes);

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
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

}
