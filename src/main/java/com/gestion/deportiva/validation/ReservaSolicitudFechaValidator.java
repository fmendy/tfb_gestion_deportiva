package com.gestion.deportiva.validation;

import java.time.LocalDate;
import java.time.LocalTime;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservaSolicitudFechaValidator
		implements ConstraintValidator<ReservaSolicitudFechaValid, ReservaSolicitudDTO> {

	@Override
	public boolean isValid(ReservaSolicitudDTO dto, ConstraintValidatorContext context) {
		if (dto.getFecha() == null || dto.getHora() == null)
			return true;

		LocalDate hoy = LocalDate.now();
		context.disableDefaultConstraintViolation(); // IMPORTANTE: Para no duplicar mensajes

		// 1. Validar fecha pasada
		if (dto.getFecha().isBefore(hoy)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.reserva.solicitud.fecha"))
					.addConstraintViolation();
			return false;
		}

		// 2. Si es hoy, validar que la hora no haya pasado
		// Fallamos si la hora de la reserva es anterior a la hora actual
		if (dto.getFecha().equals(hoy) && dto.getHora().isBefore(LocalTime.now())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.reserva.solicitud.fecha.hora")).addConstraintViolation();
			return false;
		}

		return true;
	}
}