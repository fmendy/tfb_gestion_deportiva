package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.InstalacionHorarioDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstalacionHorarioValidator implements ConstraintValidator<InstalacionHorarioValid, InstalacionHorarioDTO> {

    @Override
    public boolean isValid(InstalacionHorarioDTO dto, ConstraintValidatorContext context) {
        if (dto.getHoraInicio() == null || dto.getHoraFin() == null) {
            return true; // Dejamos que @NotNull gestione los nulos
        }
        return dto.getHoraInicio().isBefore(dto.getHoraFin());
    }
}