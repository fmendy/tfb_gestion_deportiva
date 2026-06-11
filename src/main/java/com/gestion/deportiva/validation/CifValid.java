package com.gestion.deportiva.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CifValidator.class)
public @interface CifValid {
    String message() default "{error.validacion.cif.invalido}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}