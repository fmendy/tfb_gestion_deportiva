package com.gestion.deportiva.controller.privado.sancion;

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
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.dto.SancionTipoDTO;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.SancionService;
import com.gestion.deportiva.service.SancionTipoService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.Valid;

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
	
	@PostMapping("/guardar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Sancion.GESTION_SANCION_INSTALACION  + "')")
	public ModelAndView guardar(@Valid @ModelAttribute("form") SancionDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws PermisoException {
		if (!sancionService.canWrite(dto.getId())) {
			logger.error("Sancion {} intentó acceder a una sancion  sin permisos: usuario {}",
					SecurityUtil.getCurrentUserId(), dto.getId());
			throw new PermisoException("No tiene permisos para acceder a esta sancion.");
		}
		logger.info("Guardando datos de la sancion id: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsForm(dto);
		}
		try {
			Long id = sancionService.guardar(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL + "/" + id + "/editar"));
		} catch (Exception e) {
			logger.error("Error al guardar la Instalacion : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
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
