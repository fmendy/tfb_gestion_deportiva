package com.gestion.deportiva.controller.privado.sancion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestion.deportiva.controller.BaseController;

import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.dto.SancionTipoDTO;

import com.gestion.deportiva.service.SancionService;
import com.gestion.deportiva.service.SancionTipoService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;

@Controller
@RequestMapping(value = "/privado/sancion")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Sancion.GESTION_SANCION + "')")
public class PrivadoSancionController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoSancionController.class);

	private static final String BASE_URL = "/privado/sancion";

	private static final String TITLE_PAGE = "page.title.privado.sancion";

	private static final String VIEW_FORM = "privado/sancion/form";

	private static final String VIEW_LIST = "privado/sancion/list";

	@Autowired
	private SancionService sancionService;

	@Autowired
	private SancionTipoService sancionTipoService;

	@GetMapping("/crear")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Sancion.GESTION_SANCION_INSTALACION + "')")
	public ModelAndView solicitud(RedirectAttributes redirectAttributes, SancionDTO dto) {
		logger.info("Creando sancion reserva id {}, por el usuario {}", dto.getReservaId(),
				SecurityUtil.getCurrentUserId());
		return buildDetailsForm(dto);

	}

	private ModelAndView buildDetailsForm(SancionDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form", sancionService.findByDTO(dto));
		mav.addObject("listSancionTipo",
				Utils.addEmptyOptionIfMoreThanOneOption(sancionTipoService.getListDTO(), SancionTipoDTO.class));
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

}
