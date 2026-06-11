package com.gestion.deportiva.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.MiPerfilPasswordDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsuarioCambioPasswordValidator
		implements ConstraintValidator<UsuarioCambioPasswordValid, MiPerfilPasswordDTO> {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean isValid(MiPerfilPasswordDTO dto, ConstraintValidatorContext context) {

		if (!StringUtils.hasText(dto.getPasswordNuevo()) || !StringUtils.hasText(dto.getPasswordNuevoRepetido())
				|| !StringUtils.hasText(dto.getPasswordActual())) {
			return false;
		}
		Usuario model = repository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());

		if (!passwordEncoder.matches(dto.getPasswordActual(), model.getPassword())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.cambio.password.actual"))
					.addPropertyNode("passwordActual").addConstraintViolation();
			return false;
		}

		if (!dto.getPasswordNuevo().equals(dto.getPasswordNuevoRepetido())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.cambio.password.nueva.coincide"))
					.addPropertyNode("passwordNuevo").addConstraintViolation();
			return false;
		}

		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$";

		if (!dto.getPasswordNuevo().matches(passwordRegex)) {
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.cambio.password.nueva.complejidad"))
					.addPropertyNode("passwordNuevo").addConstraintViolation();
			return false;
		}

		return false;
	}
}