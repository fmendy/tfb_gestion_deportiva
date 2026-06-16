package com.gestion.deportiva.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InstalacionHorarioValidator.class)
public @interface InstalacionHorarioValid {
    String message() default "{error.validacion.instalacion.horario.fin.despues.inicio}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}