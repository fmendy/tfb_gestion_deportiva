package com.gestion.deportiva.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InstalacionHorarioBloqueadoValidator.class)
public @interface InstalacionHorarioBloqueadoValid {
    String message() default "{error.validacion.instalacion.horario.bloqueado}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}