package com.gestion.deportiva.controller.publico;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gestion.deportiva.controller.BaseController;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/publico/inicio")
public class PublicoInicioController extends BaseController {
	
	private static final String VIEW_LIST = "inicio/home";

	@GetMapping("")
	public ModelAndView displayAll(Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		return mav;
	}
}
