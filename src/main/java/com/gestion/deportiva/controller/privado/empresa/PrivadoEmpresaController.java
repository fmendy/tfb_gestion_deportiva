package com.gestion.deportiva.controller.privado.empresa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import com.gestion.deportiva.controller.BaseController;
import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.EmpresaUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/privado/empresa")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Localizacion.GESTION_EMPRESA + "')")
public class PrivadoEmpresaController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoEmpresaController.class);

	private static final String BASE_URL = "/privado/empresa";

	private static final String TITLE_PAGE = "page.title.privado.empresa";

	private static final String VIEW_LIST = "privado/empresa/list";
	
	@Autowired
	private EmpresaService empresaService;


	@GetMapping("")
	public ModelAndView search(Pageable pageable, HttpServletRequest request, EmpresaFilter filter) {
		logger.info("Mostrando vista de listado de libro con filtros");
		return buildListView(filter, pageable, request);
	}




	private ModelAndView buildListView(EmpresaFilter filter, Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		mav.addObject("page", empresaService.getPageByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("url", EmpresaUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.empresa", null).build());
		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}
}
