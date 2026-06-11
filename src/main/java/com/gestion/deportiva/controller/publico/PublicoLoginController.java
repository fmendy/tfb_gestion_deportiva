package com.gestion.deportiva.controller.publico;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gestion.deportiva.controller.BaseController;

@Controller
public class PublicoLoginController extends BaseController {

	private static final String VIEW_FORM = "login/form";

	private static final String PAGE_TITLE = "page.title.login";

	@GetMapping(value = "/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView(VIEW_FORM);
		addBasicModelDetails(mav, PAGE_TITLE);
		return mav;
	}

}
