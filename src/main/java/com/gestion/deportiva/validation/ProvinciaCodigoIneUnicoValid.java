package com.gestion.deportiva.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;



@Documented
@Constraint(validatedBy = ProvinciaCodigoIneUnicoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvinciaCodigoIneUnicoValid {
    String message() default "{error.validacion.provincia.codigo.ine.unico}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
