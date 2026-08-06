package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.model.Sancion;
import com.gestion.deportiva.repository.SancionRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SancionValidator implements ConstraintValidator<SancionValid, SancionDTO> {

	private final SancionRepository repository;

	SancionValidator(SancionRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean isValid(SancionDTO form, ConstraintValidatorContext context) {

		Sancion sancion = repository.findByActivoTrueAndReservaId(form.getReservaId());

		if (sancion != null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.sancion.reserva.existe"))
					.addPropertyNode("reservaId").addConstraintViolation();
			return false;
		}

		if (form.getFechaFin().isBefore(form.getFechaInicio())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.sancion.fecha.inicio"))
					.addPropertyNode("fechaInicio").addConstraintViolation();
			return false;
		}
		return true;
	}
}