package com.gestion.deportiva.controller.publico;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gestion.deportiva.controller.BaseController;
import com.gestion.deportiva.dto.RegistroEmpresaDTO;

@Controller
@RequestMapping(value = "/publico/registro")
public class PublicoRegistroController extends BaseController {

	private static final String VIEW_FORM = "registro/empresa/form";

	private static final String PAGE_TITLE_EMPRESA = "page.title.registro.empresa";
	
	private static final String PAGE_TITLE_USUARIO = "page.title.registro.usuario";

	@GetMapping(value = "/usuario")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		addBasicModelDetails(mav, PAGE_TITLE_USUARIO);
		return mav;
	}
	
	@GetMapping(value = "/empresa")
	public ModelAndView registroEmpresa() {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		addBasicModelDetails(mav, PAGE_TITLE_EMPRESA);
		mav.addObject("form", new RegistroEmpresaDTO());
		return mav;
	}

}
