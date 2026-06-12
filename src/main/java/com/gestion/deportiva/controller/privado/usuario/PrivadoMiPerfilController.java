package com.gestion.deportiva.controller.privado.usuario;

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
import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.dto.RegistroEmpresaDTO;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/usuario/miperfil")
@PreAuthorize("isAuthenticated() and hasAuthority('" + Constantes.Permiso.Usuario.MI_PERFIL + "')")
public class PrivadoMiPerfilController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoMiPerfilController.class);

	private static final String TITLE_PAGE = "page.title.mi.perfil";

	private static final String VIEW_FORM = "usuario/miperfil/form";
	
	private static final String BASE_URL = "/privado/usuario/miperfil";

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ModelAndView verMiPerfil() {
		logger.info("Usuario autenticado viendo su perfil: {}", SecurityUtil.getCurrentUser().getUsername());
		return buildDetailsMiPerilForm(usuarioService.getMiPerfilDTO());
	}

	@PostMapping("/guardar")
	public ModelAndView guardar(@Valid @ModelAttribute("form") MiPerfilDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		logger.info("Actualizando datos del usuario: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsMiPerilForm(dto);
		}
		try {
			usuarioService.actualizarMiPerfil(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL));
		} catch (Exception e) {
			logger.error("Error al actualizar datos de perfil : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsMiPerilForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	private ModelAndView buildDetailsMiPerilForm(MiPerfilDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		addBasicModelDetails(mav, TITLE_PAGE);
		mav.addObject("form", dto);
		return mav;
	}

}
