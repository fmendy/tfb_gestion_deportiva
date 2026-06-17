package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.InstalacionConfiguracionReservaDTO;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstalacionConfiguracionReservaValidator implements ConstraintValidator<InstalacionConfiguracionReservaValid, InstalacionConfiguracionReservaDTO> {

    @Override
    public boolean isValid(InstalacionConfiguracionReservaDTO dto, ConstraintValidatorContext context) {
        if (dto.getDuracionMin() == null || dto.getDuracionMax() == null) {
            return true; 
        }
        
        if(dto.getDuracionMin() > dto.getDuracionMax()) {
        	 context.disableDefaultConstraintViolation();
     		context.buildConstraintViolationWithTemplate(Utils.getMessage("error.validacion.instalacion.configuracion.reserva.duracion.minima"))
     				.addPropertyNode("duracionMin").addConstraintViolation();
     		return false;
        	
        }
       
        return true;
    }
}