package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.repository.SancionRepository;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservaSolicitudSancionValidator
		implements ConstraintValidator<ReservaSolicitudSancionValid, ReservaSolicitudDTO> {

	private final SancionRepository repository;

	ReservaSolicitudSancionValidator(SancionRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean isValid(ReservaSolicitudDTO dto, ConstraintValidatorContext context) {

		boolean estaSancionado = repository
				.existsByActivoTrueAndUsuarioIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
						SecurityUtil.getCurrentUserId(), dto.getFecha(), dto.getFecha());

		// Horario Especial de la instalación
		if (estaSancionado) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.reserva.sancion"))
					.addConstraintViolation();
			return false;
		}

		return true;
	}
}