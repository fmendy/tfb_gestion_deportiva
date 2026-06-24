package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstalacionHorarioBloqueadoValidator
		implements ConstraintValidator<InstalacionHorarioBloqueadoValid, InstalacionHorarioBloqueadoDTO> {

	@Override
	public boolean isValid(InstalacionHorarioBloqueadoDTO dto, ConstraintValidatorContext context) {


		if (dto.getHoraFin().isBefore(dto.getHoraInicio())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.instalacion.horario.hora.inicio.mayor.hora.fin"))
					.addPropertyNode("horaInicio").addConstraintViolation();
			return false;

		}

		return true;
	}
}