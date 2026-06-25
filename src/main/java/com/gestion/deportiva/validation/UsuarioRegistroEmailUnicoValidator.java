package com.gestion.deportiva.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsuarioRegistroEmailUnicoValidator
		implements ConstraintValidator<UsuarioRegistroEmailUnicoValid, UsuarioRegistroDTO> {

	@Autowired
	private UsuarioRepository repository;

	@Override
	public boolean isValid(UsuarioRegistroDTO form, ConstraintValidatorContext context) {
		String uuidActual = form.getUuid();

		Usuario model = repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(form.getEmail(), form.getId());

		if (model == null) {
			return true; // No existe => válido
		}
		// Si estamos editando el mismo registro, es válido
		if (StringUtils.hasText(uuidActual) && uuidActual.equals(model.getUuid())) {
			return true;
		}
		// Existe otro con el mismo nombre y mismo padre
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.registro.usuario.email.unico"))
				.addPropertyNode("email").addConstraintViolation();
		return false;
	}
}