package com.gestion.deportiva.controller;

import java.util.Date;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.Utils;




public class BaseController {

	public void addBasicModelDetails(ModelAndView mav, String title) {
		mav.addObject(Constantes.ANNO, Utils.dateToAnno(new Date()));
		mav.addObject(Constantes.PAGE_TITLE, Utils.getMessage(title));

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String username = "";
		if (authentication.isAuthenticated()) {
			username = authentication.getName();
		}
		mav.addObject(Constantes.USERNAME, username);
	}
	
	public void addBasicModelDetails(ModelAndView mav, String title, boolean isAddBasicBreadCrumb) {
		addBasicModelDetails(mav, title);
		if(isAddBasicBreadCrumb) {
			addBasicBreadcrumb(mav);
		}
	}

	public void addBasicBreadcrumb(ModelAndView mav) {
		mav.addObject("breadcrumbs", BreadcrumbBuilder.start().includeHome().build());
	}

	public void addSortParameter(ModelAndView mav, Pageable pageable) {
		mav.addObject("sort", Utils.getParameterSort(pageable.getSort()));
	}

	public ModelAndView redirectWithError(String url, RedirectAttributes redirectAttributes, int errorCode,
			String errorKey) {
		redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, errorCode);
		if (errorCode == Constantes.CUSTOM_ERROR_CODE) {
			redirectAttributes.addFlashAttribute(Constantes.CUSTOM_ERROR, Utils.getMessage(errorKey));
		}
		return new ModelAndView(new RedirectView(url));
	}
}
