package com.gestion.deportiva.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;



@Component
public class AuditoriaInterceptor implements HandlerInterceptor {

	/*@Autowired
	private AuditoriaWebService auditoriaWebService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		AuditoriaWebForm auditoriaWebForm = new AuditoriaWebForm();
		auditoriaWebForm.setUrl(request.getRequestURI());
		auditoriaWebForm.setReferer(request.getHeader("Referer"));
		auditoriaWebForm.setIp(request.getRemoteAddr());
		auditoriaWebForm.setUserAgent(request.getHeader("User-Agent"));

		if (SecurityUtil.isAuthenticated()) {
			auditoriaWebForm.setUsuarioId(SecurityUtil.getCurrentUserId());
		}
		auditoriaWebService.guardar(auditoriaWebForm);

		return true;
	}*/
}
