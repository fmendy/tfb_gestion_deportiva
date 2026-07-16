package com.gestion.deportiva.controller.publico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
import com.gestion.deportiva.dto.UsuarioPasswordDTO;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.Constantes;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/publico/password")
public class PublicoPasswordController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PublicoPasswordController.class);

	private static final String VIEW_USUARIO_FORM = "publico/password/form";

	private static final String PAGE_TITLE_USUARIO = "page.title.publico.password.usuario";

	private static final String BASE_URL = "/publico/password";


	private final UsuarioService usuarioService;

	PublicoPasswordController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@GetMapping("")
	public ModelAndView reset() {
			return buildDetailsPasswordUsuarioForm(new UsuarioPasswordDTO());
	}
	private ModelAndView buildDetailsPasswordUsuarioForm(UsuarioPasswordDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_USUARIO_FORM);
		addBasicModelDetails(mav, PAGE_TITLE_USUARIO);
		mav.addObject("form", dto);
		return mav;
	}

	@PostMapping("/reset")
	public ModelAndView reset(@Valid @ModelAttribute("form") UsuarioPasswordDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		logger.info("Comienzo del proceso de reseteo de la password del usuario {}", dto.getEmail());
		if (bindingResult.hasErrors()) {
			return buildDetailsPasswordUsuarioForm(dto);
		}
		try {
			usuarioService.enviarMailPasswordOlvidada(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL));
		} catch (Exception e) {
			logger.error("Error al enviar mail de reseteo : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsPasswordUsuarioForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}
	
	@GetMapping("/resetear")
	public ModelAndView resetear(@Valid @ModelAttribute("form") UsuarioPasswordDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		logger.info("Seggunda parte  del proceso de reseteo de la password del usuario {}", dto.getEmail());

		try {
			usuarioService.generarPasswordYEnviarMail(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL));
		} catch (Exception e) {
			logger.error("Error al enviar mail de reseteo : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsPasswordUsuarioForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

}
