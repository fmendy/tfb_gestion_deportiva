package com.gestion.deportiva.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.repository.EmpresaRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmpresaNombreUnicoValidator implements ConstraintValidator<EmpresaNombreUnicoValid, EmpresaDTO> {

	@Autowired
	private EmpresaRepository repository;

	@Override
	public boolean isValid(EmpresaDTO form, ConstraintValidatorContext context) {
		String uuidActual = form.getUuid();

		Empresa model = repository.findByActivoTrueAndNombreEqualsIgnoreCaseAndIdNot(form.getNombre(), form.getId());

		if (model == null) {
			return true; // No existe => válido
		}
		// Si estamos editando el mismo registro, es válido
		if (StringUtils.hasText(uuidActual) && uuidActual.equals(model.getUuid())) {
			return true;
		}
		// Existe otro con el mismo nombre y mismo padre
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.empresa.nombre.unico"))
				.addPropertyNode("nombre").addConstraintViolation();
		return false;
	}
}