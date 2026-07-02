package com.gestion.deportiva.validation;


import org.springframework.beans.factory.annotation.Autowired;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservaSolicitudDisponibilidadValidator
		implements ConstraintValidator<ReservaSolicitudDisponibilidadValid, ReservaSolicitudDTO> {

	@Autowired
	private ReservaService reservaService;

	@Override
	public boolean isValid(ReservaSolicitudDTO dto, ConstraintValidatorContext context) {

		if (!reservaService.isFranjaHorariaDisponibleParaInstalacion(dto.getFecha(), dto.getHora(), dto.getDuracion(),
				dto.getInstalacionId())) {
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.reserva.disponibilidad.instalacion")).addConstraintViolation();
			return false;
		}

		if (!reservaService.isFranjaHorariaDisponibleParaUsuario(dto.getFecha(), dto.getHora(), dto.getDuracion(),
				SecurityUtil.getCurrentUserId())) {
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.reserva.disponibilidad.usuario")).addConstraintViolation();
			return false;
		}

		return true;
	}
}