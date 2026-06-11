package com.gestion.deportiva.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComunidadAutonomaDTO;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.repository.ComunidadAutonomaRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ComunidadAutonomaNombreUnicoValidator
		implements ConstraintValidator<ComunidadAutonomaNombreUnicoValid, ComunidadAutonomaDTO> {

	@Autowired
	private ComunidadAutonomaRepository comunidadAutonomaRepository;

	@Override
	public boolean isValid(ComunidadAutonomaDTO form, ConstraintValidatorContext context) {
		String uuidActual = form.getUuid();

		if (form.getNombre() == null) {
			return true; // @NotBlank lo valida
		}
		ComunidadAutonoma model = comunidadAutonomaRepository
				.findByActivoTrueAndNombreEqualsIgnoreCase(form.getNombre());

		if (model == null) {
			return true; // No existe => válido
		}
		// Si estamos editando el mismo registro, es válido
		if (StringUtils.hasText(uuidActual) && uuidActual.equals(model.getUuid())) {
			return true;
		}
		// Existe otro con el mismo nombre y mismo padre
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(
				Utils.getMessage("error.validacion.comunidad.autonoma.nombre.unico")).addPropertyNode("nombre")
				.addConstraintViolation();
		return false;
	}
}