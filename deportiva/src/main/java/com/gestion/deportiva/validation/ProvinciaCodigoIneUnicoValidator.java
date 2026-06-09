package com.gestion.deportiva.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ProvinciaDTO;
import com.gestion.deportiva.model.Provincia;
import com.gestion.deportiva.repository.ProvinciaRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProvinciaCodigoIneUnicoValidator
		implements ConstraintValidator<ProvinciaCodigoIneUnicoValid, ProvinciaDTO> {

	@Autowired
	private ProvinciaRepository provinciaRepository;

	@Override
	public boolean isValid(ProvinciaDTO form, ConstraintValidatorContext context) {
		String uuidActual = form.getUuid();

		if (form.getCodigoIne() == null) {
			return true; // @NotBlank lo valida
		}
		Provincia model = provinciaRepository.findByActivoTrueAndCodigoIne(form.getCodigoIne());

		if (model == null) {
			return true; // No existe => válido
		}
		// Si estamos editando el mismo registro, es válido
		if (StringUtils.hasText(uuidActual) && uuidActual.equals(model.getUuid())) {
			return true;
		}
		// Existe otro con el mismo nombre y mismo padre
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.provincia.codigo.ine.unico"))
				.addPropertyNode("codigoIne").addConstraintViolation();
		return false;
	}
}