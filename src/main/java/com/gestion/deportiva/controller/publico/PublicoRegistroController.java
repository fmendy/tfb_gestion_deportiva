package com.gestion.deportiva.controller.publico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.service.RegistroEmpresaService;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.Constantes;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/publico/registro")
public class PublicoRegistroController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PublicoRegistroController.class);

	private static final String VIEW_EMPRESA_FORM = "publico/registro/empresaForm";

	private static final String VIEW_USUARIO_FORM = "publico/registro/usuarioForm";

	private static final String PAGE_TITLE_EMPRESA = "page.title.registro.empresa";

	private static final String PAGE_TITLE_USUARIO = "page.title.registro.usuario";

	@Autowired
	private RegistroEmpresaService registroEmpresaService;
	
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping(value = "/usuario")
	public ModelAndView login() {
		return buildDetailsRegistroUsuarioForm(new UsuarioRegistroDTO());
	}

	@PostMapping("/usuario/guardar")
	public ModelAndView guardarUsuario(@Valid @ModelAttribute("form") UsuarioRegistroDTO dto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		logger.info("Registrando empresa id: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsRegistroUsuarioForm(dto);
		}
		try {
			usuarioService.registrarUsuarioCliente(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView("/login"));
		} catch (Exception e) {
			logger.error("Error al registrar al usuario: {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsRegistroUsuarioForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

	private ModelAndView buildDetailsRegistroUsuarioForm(UsuarioRegistroDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_USUARIO_FORM);
		addBasicModelDetails(mav, PAGE_TITLE_USUARIO);
		mav.addObject("form", dto);
		return mav;
	}

	@GetMapping(value = "/empresa")
	public ModelAndView registroEmpresa() {
		return buildDetailsRegistroEmpresaForm(new EmpresaRegistroDTO());
	}

	private ModelAndView buildDetailsRegistroEmpresaForm(EmpresaRegistroDTO dto) {
		ModelAndView mav = new ModelAndView(VIEW_EMPRESA_FORM);
		addBasicModelDetails(mav, PAGE_TITLE_EMPRESA);
		mav.addObject("form", dto);
		return mav;
	}

	@PostMapping("/empresa/guardar")
	public ModelAndView guardar(@Valid @ModelAttribute("form") EmpresaRegistroDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		logger.info("Registrando empresa id: {}", dto.getId());
		if (bindingResult.hasErrors()) {
			return buildDetailsRegistroEmpresaForm(dto);
		}
		try {
			registroEmpresaService.registrarEmpresa(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView("/login"));
		} catch (Exception e) {
			logger.error("Error al registrar la empresa : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsRegistroEmpresaForm(dto);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}
	}

}
