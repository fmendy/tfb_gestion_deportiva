package com.gestion.deportiva.controller.privado.reserva;

import java.util.function.BooleanSupplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.gestion.deportiva.controller.BaseController;
import com.gestion.deportiva.dto.InstalacionTipoDTO;
import com.gestion.deportiva.dto.ReservaEstadoDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;
import com.gestion.deportiva.exception.PermisoException;
import com.gestion.deportiva.service.InstalacionTipoService;
import com.gestion.deportiva.service.ReservaEstadoService;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.BreadcrumbBuilder;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.ReservaUtil;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/privado/reserva")
@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA + "')")
public class PrivadoReservaController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PrivadoReservaController.class);

	private static final String BASE_URL = "/privado/reserva";

	private static final String TITLE_PAGE = "page.title.privado.reserva.instalacion";

	private static final String VIEW_SOLICITUD_FORM = "privado/reserva/solicitudForm";

	private static final String VIEW_HISTORICO_LIST = "privado/reserva/historicoList";

	private static final String VIEW_MIS_RESERVAS_LIST = "privado/reserva/misReservasList";

	private static final String VIEW_LIST = "privado/reserva/list";

	private final ReservaService reservaService;

	private final InstalacionTipoService instalacionTipoService;

	private final ReservaEstadoService reservaEstadoService;

	PrivadoReservaController(ReservaService reservaService, InstalacionTipoService instalacionTipoService,
			ReservaEstadoService reservaEstadoService) {
		this.reservaService = reservaService;
		this.instalacionTipoService = instalacionTipoService;
		this.reservaEstadoService = reservaEstadoService;
	}

	@GetMapping("/solicitud")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA + "')")
	public ModelAndView solicitud(RedirectAttributes redirectAttributes, ReservaSolicitudDTO dto) {
		logger.info("accediendo a la reserva de una instalacionid {}, por el usuario {}", dto.getInstalacionId(),
				SecurityUtil.getCurrentUserId());
		return buildDetailsSolicitudForm(dto, null);

	}

	@PostMapping("/solicitar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA + "')")
	public ModelAndView solicitar(@Valid @ModelAttribute("form") ReservaSolicitudDTO dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws PermisoException {

		logger.info("Creando solicitud de reserva en la instalacion id: {}, para el usuario id", dto.getInstalacionId(),
				SecurityUtil.getCurrentUserId());
		if (bindingResult.hasErrors()) {
			return buildDetailsSolicitudForm(dto, bindingResult);
		}
		try {
			reservaService.crearReservaEstadoPendiente(dto);
			redirectAttributes.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(BASE_URL + "/misreservas"));
		} catch (Exception e) {
			logger.error("Error al guardar la Instalacion : {}", e.getMessage(), e);
			ModelAndView mav = buildDetailsSolicitudForm(dto, null);
			mav.addObject(Constantes.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return mav;
		}

	}

	@GetMapping("/misreservas")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA + "')")
	public ModelAndView misReservas(Pageable pageable, HttpServletRequest request) {
		logger.info("accediendo a las reservas actuales del usuario {}", SecurityUtil.getCurrentUserId());
		return buildListMisReservasView(reservaService.getReservaFilterParaMisReservas(), pageable, request, false);

	}

	@GetMapping("/misreservas/pasadas")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA + "')")
	public ModelAndView misReservasPasAndView(Pageable pageable, HttpServletRequest request) {
		logger.info("accediendo a las reservas actuales del usuario {}", SecurityUtil.getCurrentUserId());
		return buildListMisReservasView(reservaService.getReservaFilterParaMisReservasPasadas(), pageable, request,
				true);

	}

	@GetMapping("/{id}/historico")
	public ModelAndView verHistorico(@PathVariable Long id, RedirectAttributes ra) {
		logger.info("accediendo a las reserva {} para ver historico del usuario {}", id,
				SecurityUtil.getCurrentUserId());
		return buildListHistoricoView(id);

	}

	private ModelAndView ejecutarAccion(Long id, RedirectAttributes ra, String redirectUrl,
			BooleanSupplier checkPermiso, Runnable accion, String errorMsg) throws PermisoException {

		if (!checkPermiso.getAsBoolean()) {
			logger.error("Usuario {} intentó acceder sin permisos a la reserva {}", SecurityUtil.getCurrentUserId(),
					id);
			throw new PermisoException("No tiene permisos para realizar esta acción.");
		}

		try {
			accion.run();
			ra.addFlashAttribute(Constantes.HTTP_STATUS, HttpStatus.OK.value());
			return new ModelAndView(new RedirectView(redirectUrl));
		} catch (Exception e) {
			logger.error(errorMsg + " {}", id, e);
			return redirectWithError(redirectUrl, ra, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@GetMapping("/{id}/cancelarusuario")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA + "')")
	public ModelAndView cancelarUsuario(@PathVariable Long id, RedirectAttributes ra) throws PermisoException {
		return ejecutarAccion(id, ra, BASE_URL + "/misreservas", () -> reservaService.canCancelarUsuario(id),
				() -> reservaService.cancelarUsuario(id), "Error al cancelar la reserva por usuario");
	}

	@GetMapping("/{id}/aprobar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION + "')")
	public ModelAndView aprobar(@PathVariable Long id, RedirectAttributes ra) throws PermisoException {
		return ejecutarAccion(id, ra, BASE_URL, () -> reservaService.canAprobarDenegarReserva(id),
				() -> reservaService.aprobar(id), "Error al aprobar la reserva");
	}

	@GetMapping("/{id}/cancelarempresa")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION + "')")
	public ModelAndView cancelarEmpresa(@PathVariable Long id, RedirectAttributes ra) throws PermisoException {
		return ejecutarAccion(id, ra, BASE_URL, () -> reservaService.canCancelarCompletadaIncompletadaEmpresa(id),
				() -> reservaService.cancelarEmpresa(id), "Error al cancelar la reserva por empresa");
	}

	@GetMapping("/{id}/completar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION + "')")
	public ModelAndView completar(@PathVariable Long id, RedirectAttributes ra) throws PermisoException {
		return ejecutarAccion(id, ra, BASE_URL, () -> reservaService.canCancelarCompletadaIncompletadaEmpresa(id),
				() -> reservaService.completar(id), "Error al completar la reserva");
	}

	@GetMapping("/{id}/denegar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION + "')")
	public ModelAndView denegar(@PathVariable Long id, RedirectAttributes ra) throws PermisoException {
		return ejecutarAccion(id, ra, BASE_URL, () -> reservaService.canAprobarDenegarReserva(id),
				() -> reservaService.denegar(id), "Error al denegar la reserva");
	}

	@GetMapping("/{id}/incompletar")
	@PreAuthorize("hasAuthority('" + Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION + "')")
	public ModelAndView incompletar(@PathVariable Long id, RedirectAttributes ra) throws PermisoException {
		return ejecutarAccion(id, ra, BASE_URL, () -> reservaService.canCancelarCompletadaIncompletadaEmpresa(id),
				() -> reservaService.incompletar(id), "Error al completar la reserva");
	}

	@GetMapping("")
	public ModelAndView search(Pageable pageable, HttpServletRequest request, ReservaFilter filter) {
		logger.info("Mostrando vista de listado de reserva con filtros, usuario {}", SecurityUtil.getCurrentUserId());
		return buildListView(filter, pageable, request);
	}

	private ModelAndView buildListView(ReservaFilter filter, Pageable pageable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(VIEW_LIST);
		mav.addObject("page", reservaService.getPageListadoByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("url", ReservaUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.gestion.reserva", null).build());
		mav.addObject("listInstalacionTipo",
				Utils.addEmptyOptionIfMoreThanOneOption(instalacionTipoService.getListDTO(), InstalacionTipoDTO.class));
		mav.addObject("listReservaEstado",
				Utils.addEmptyOptionIfMoreThanOneOption(reservaEstadoService.getListDTO(), ReservaEstadoDTO.class));
		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildDetailsSolicitudForm(ReservaSolicitudDTO dto, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView(VIEW_SOLICITUD_FORM);
		mav.addObject("form", reservaService.getFullReservaSolicitudDTOByReservaSolictudDTO(dto));

		if (bindingResult != null) {
			mav.addObject("org.springframework.validation.BindingResult.form", bindingResult);
		}
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildListMisReservasView(ReservaFilter filter, Pageable pageable, HttpServletRequest request,
			boolean reservasPasadas) {
		ModelAndView mav = new ModelAndView(VIEW_MIS_RESERVAS_LIST);
		mav.addObject("page", reservaService.getPageMiReservaListadoDTOByFilter(filter, pageable));
		mav.addObject("filter", filter);
		mav.addObject("reservasPasadas", reservasPasadas);
		mav.addObject("url", ReservaUtil.cleanUrlPageFilter(filter, request.getRequestURI()));
		mav.addObject("breadcrumbs",
				BreadcrumbBuilder.start().includeHome().add("breadcrumb.reservas.mis.reservas", null).build());
		addSortParameter(mav, pageable);
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

	private ModelAndView buildListHistoricoView(Long reservaId) {
		ModelAndView mav = new ModelAndView(VIEW_HISTORICO_LIST);
		mav.addObject("historico", reservaService.getListHistorico(reservaId));
		addBasicModelDetails(mav, TITLE_PAGE, false);
		return mav;
	}

}
