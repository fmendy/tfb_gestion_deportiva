package com.gestion.deportiva.controller.privado.reserva;

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
import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaInstalacionDTO;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/reserva")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA + "')")
public class PrivadoReservaController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoReservaController.class);

	private static final String BASE_URL = "/privado/reserva";

	private static final String TITLE_PAGE = "page.title.privado.reserva.instalacion";

	private static final String VIEW_FORM = "privado/reserva/reservaForm";

	@Autowired
	private ReservaService reservaService;

	@GetMapping("/instalacion/{instalacionId}")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA + "')")
	public ModelAndView reservaInstalacion(@PathVariable Long instalacionId, RedirectAttributes redirectAttributes,
			ReservaInstalacionDTO dto) {
		logger.info("accediendo a la reserva de una instalacionid {}, por el usuario {}", instalacionId,
				SecurityUtil.getCurrentUserId());
		return buildDetailsForm(instalacionId, dto);

	}

	private ModelAndView buildDetailsForm(Long instalacionId, ReservaInstalacionDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		mav.addObject("form",
				reservaService.getReservaInstalacionDTOByInstalacionIdAndReservaInstalacionDTO(instalacionId, dto));
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

}
