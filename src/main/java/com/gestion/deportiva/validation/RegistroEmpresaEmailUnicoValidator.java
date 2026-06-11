package com.gestion.deportiva.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.RegistroEmpresaDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.repository.EmpresaRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegistroEmpresaEmailUnicoValidator implements ConstraintValidator<RegistroEmpresaEmailUnicoValid, RegistroEmpresaDTO> {

	@Autowired
	private EmpresaRepository repository;

	@Override
	public boolean isValid(RegistroEmpresaDTO form, ConstraintValidatorContext context) {
		String uuidActual = form.getUuid();

		Empresa model = repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(form.getEmail(), form.getId());

		if (model == null) {
			return true; // No existe => válido
		}
		// Si estamos editando el mismo registro, es válido
		if (StringUtils.hasText(uuidActual) && uuidActual.equals(model.getUuid())) {
			return true;
		}
		// Existe otro con el mismo nombre y mismo padre
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.registro.empresa.email.unico"))
				.addPropertyNode("email").addConstraintViolation();
		return false;
	}
}