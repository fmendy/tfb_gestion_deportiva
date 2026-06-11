package com.gestion.deportiva.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.MunicipioDTO;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.repository.MunicipioRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MunicipioCodigoIneUnicoValidator
		implements ConstraintValidator<MunicipioCodigoIneUnicoValid, MunicipioDTO> {

	@Autowired
	private MunicipioRepository municipioRepository;

	@Override
	public boolean isValid(MunicipioDTO form, ConstraintValidatorContext context) {
		String uuidActual = form.getUuid();

		if (form.getCodigoIne() == null) {
			return true; // @NotBlank lo valida
		}
		Municipio model = municipioRepository.findByActivoTrueAndCodigoIneAndProvincia_Uuid(form.getCodigoIne(),
				form.getProvinciaUuid());

		if (model == null) {
			return true; // No existe => válido
		}
		// Si estamos editando el mismo registro, es válido
		if (StringUtils.hasText(uuidActual) && uuidActual.equals(model.getUuid())) {
			return true;
		}
		// Existe otro con el mismo nombre y mismo padre
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.municipio.codigo.ine.unico"))
				.addPropertyNode("codigoIne").addConstraintViolation();
		return false;
	}
}