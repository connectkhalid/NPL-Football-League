package com.nfl.national_football_league.form.annotation;

import com.nfl.national_football_league.form.validation.ImageFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageFileValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {
    String message() default "Invalid image file";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // File type and size limits
    String[] types() default { "image/jpeg", "image/png" };
    long maxSize() default 1048576; // 1MB by default
}
