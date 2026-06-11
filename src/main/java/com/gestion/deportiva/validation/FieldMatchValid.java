package com.gestion.deportiva.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Repeatable(FieldMatchValid.List.class) // <--- ESTO ES LO QUE FALTA
public @interface FieldMatchValid {
    String message() default "{error.validacion.campos.coincidencia}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String first();
    String second();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldMatchValid[] value();
    }
}