package com.gestion.deportiva.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;



@Documented
@Constraint(validatedBy = ComunidadAutonomaNombreUnicoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ComunidadAutonomaNombreUnicoValid {
    String message() default "{error.validacion.comunidad.autonoma.nombre.unico}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
