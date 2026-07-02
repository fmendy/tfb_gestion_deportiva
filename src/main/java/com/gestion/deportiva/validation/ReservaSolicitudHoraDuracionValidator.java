package com.gestion.deportiva.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.service.InstalacionConfiguracionReservaService;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservaSolicitudHoraDuracionValidator
		implements ConstraintValidator<ReservaSolicitudHoraDuracionValid, ReservaSolicitudDTO> {

	@Autowired
	private InstalacionConfiguracionReservaService instalacionConfiguracionReservaService;

	@Override
	public boolean isValid(ReservaSolicitudDTO dto, ConstraintValidatorContext context) {

		if (!instalacionConfiguracionReservaService.isValid(dto.getInstalacionId(), dto.getHora(), dto.getDuracion())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.reserva.hora.inicio.duracion")).addConstraintViolation();
			return false;
		}

		return true;
	}
}