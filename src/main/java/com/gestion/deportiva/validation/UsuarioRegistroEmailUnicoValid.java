package com.gestion.deportiva.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;



@Documented
@Constraint(validatedBy = UsuarioRegistroEmailUnicoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsuarioRegistroEmailUnicoValid {
    String message() default "{error.validacion.registro.usuario.email.unico}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
