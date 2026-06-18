package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.InstalacionHorarioEspecialDTO;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstalacionHorarioEspecialValidator
		implements ConstraintValidator<InstalacionHorarioEspecialValid, InstalacionHorarioEspecialDTO> {

	@Override
	public boolean isValid(InstalacionHorarioEspecialDTO dto, ConstraintValidatorContext context) {

		if (!dto.getCerrado() && dto.getHoraFin() == null && dto.getHoraInicio() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.instalacion.horario.no.valores")).addPropertyNode("horaInicio")
					.addConstraintViolation();
			return false;
		}

		if (dto.getCerrado() && (dto.getHoraFin() != null || dto.getHoraInicio() != null)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.instalacion.horario.especial.cerrado.hora.valor"))
					.addPropertyNode("cerrado").addConstraintViolation();
			return false;

		}

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