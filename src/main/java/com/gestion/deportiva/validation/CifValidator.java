package com.gestion.deportiva.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class CifValidator implements ConstraintValidator<CifValid, String> {

    private static final String CIF_REGEX = "^[ABCDEFGHJKLMNPQRSUVW][0-9]{7}[0-9A-J]$";

    @Override
    public boolean isValid(String cif, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(cif)) {
            return true; 
        }
        
        String normalizedCif = cif.toUpperCase().trim();
        
        return normalizedCif.matches(CIF_REGEX);
    }
}