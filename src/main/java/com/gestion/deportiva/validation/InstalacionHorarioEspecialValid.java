package com.gestion.deportiva.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InstalacionHorarioEspecialValidator.class)
public @interface InstalacionHorarioEspecialValid {
    String message() default "{error.validacion.instalacion.horario.especial}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}