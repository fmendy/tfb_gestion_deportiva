package com.gestion.deportiva.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InstalacionConfiguracionReservaValidator.class)
public @interface InstalacionConfiguracionReservaValid {
    String message() default "{error.validacion.instalacion.configuracion.reserva}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}