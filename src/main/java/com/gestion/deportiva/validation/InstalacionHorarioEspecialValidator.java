package com.gestion.deportiva.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.gestion.deportiva.dto.InstalacionHorarioEspecialDTO;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;
import com.gestion.deportiva.repository.InstalacionHorarioEspecialRepository;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstalacionHorarioEspecialValidator
		implements ConstraintValidator<InstalacionHorarioEspecialValid, InstalacionHorarioEspecialDTO> {

	@Autowired
	private InstalacionHorarioEspecialRepository repository;

	@Override
	public boolean isValid(InstalacionHorarioEspecialDTO dto, ConstraintValidatorContext context) {

		InstalacionHorarioEspecial instalacionHorarioEspecial = repository
				.findByActivoTrueAndCerradoTrueAndFecha(dto.getFecha());

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
					.addPropertyNode(Constantes.CERRADO).addConstraintViolation();
			return false;

		}

		if (!dto.getCerrado() && dto.getHoraFin().isBefore(dto.getHoraInicio())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.instalacion.horario.hora.inicio.mayor.hora.fin"))
					.addPropertyNode("horaInicio").addConstraintViolation();
			return false;

		}

		if (!dto.getCerrado() && instalacionHorarioEspecial != null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.instalacion.horario.especial.cerrado.existente"))
					.addPropertyNode(Constantes.CERRADO).addConstraintViolation();
			return false;
		}

		if (dto.getCerrado() && instalacionHorarioEspecial != null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.instalacion.horario.especial.cerrado.duplicado"))
					.addPropertyNode(Constantes.CERRADO ).addConstraintViolation();
			return false;
		}

		return true;
	}
}