package com.gestion.deportiva.controller.publico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
