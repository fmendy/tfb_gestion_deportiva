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

public class MiPerfilPasswordValidator
		implements ConstraintValidator<MiPerfilPasswordValid, MiPerfilPasswordDTO> {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean isValid(MiPerfilPasswordDTO dto, ConstraintValidatorContext context) {

		if (!StringUtils.hasText(dto.getPassword()) || !StringUtils.hasText(dto.getPasswordConfirmar())
				|| !StringUtils.hasText(dto.getPasswordActual())) {
			return false;
		}
		Usuario model = repository.findByActivoTrueAndId(dto.getId());

		if (!passwordEncoder.matches(dto.getPasswordActual(), model.getPassword())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.cambio.password.actual"))
					.addPropertyNode("passwordActual").addConstraintViolation();
			return false;
		}

		if (!dto.getPassword().equals(dto.getPasswordConfirmar())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.cambio.password.nueva.coincide")).addPropertyNode("password")
					.addConstraintViolation();
			return false;
		}

		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$";

		if (!dto.getPassword().matches(passwordRegex)) {
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.cambio.password.nueva.complejidad")).addPropertyNode("password")
					.addConstraintViolation();
			return false;
		}

		return true;
	}
}