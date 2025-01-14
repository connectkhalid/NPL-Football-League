package com.nfl.national_football_league.form.validation;

import com.nfl.national_football_league.form.annotation.ValidImage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private String[] types;
    private long maxSize;

    @Override
    public void initialize(ValidImage constraint) {
        this.types = constraint.types();
        this.maxSize = constraint.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Check file type
        if (!Arrays.asList(types).contains(file.getContentType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid file type. Allowed types are: " + String.join(", ", types))
                    .addConstraintViolation();
            return false;
        }

        // Check file size
        if (file.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size exceeds the maximum limit of " + maxSize / 1024 + " KB")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
