package com.gestion.deportiva.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;



@Documented
@Constraint(validatedBy = ReservaSolicitudSancionValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReservaSolicitudSancionValid {
    String message() default "{error.validacion.reserva.sancion}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
