package com.gestion.deportiva.controller.privado.reserva;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.gestion.deportiva.controller.BaseController;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/reserva")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA + "')")
public class PrivadoReservaController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoReservaController.class);

	private static final String BASE_URL = "/privado/reserva";

	private static final String TITLE_PAGE = "page.title.privado.reserva.instalacion";

	private static final String VIEW_SOLICITUD_FORM = "privado/reserva/solicitudForm";

	@Autowired
	private ReservaService reservaService;

	@GetMapping("/solicitud")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA + "')")
	public ModelAndView solicitud(RedirectAttributes redirectAttributes, ReservaSolicitudDTO dto) {
		logger.info("accediendo a la reserva de una instalacionid {}, por el usuario {}", dto.getInstalacionId(),
				SecurityUtil.getCurrentUserId());
		return buildDetailsSolicitudForm(dto, null);

	}

	@PostMapping("/solicitar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA + "')")
	public ModelAndView solicitar(@Valid @ModelAttribute("form") ReservaSolicitudDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws PermisoException {

		logger.info("Creando solicitud de reserva en la instalacion id: {}, para el usuario id", dto.getInstalacionId(),
				SecurityUtil.getCurrentUserId());
		if (bindingResult.hasErrors()) {
			return buildDetailsSolicitudForm(dto, bindingResult);
		}
		try {
			Long id = reservaService.crearReservaEstadoPendiente(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL + "/" + id + "/editar"));
		} catch (Exception e) {
			logger.error("Error al guardar la Instalacion : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsSolicitudForm(dto, null);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}

	}

	private ModelAndView buildDetailsSolicitudForm(ReservaSolicitudDTO dto, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView(VIEW_SOLICITUD_FORM);
		mav.addObject("form", reservaService.getFullReservaSolicitudDTOByReservaSolictudDTO(dto));

		if (bindingResult != null) {
			mav.addObject("org.springframework.validation.BindingResult.form", bindingResult);
		}
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

}
