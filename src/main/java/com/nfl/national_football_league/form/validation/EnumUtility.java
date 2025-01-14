package com.nfl.national_football_league.form.validation;

import java.util.Locale;

public class EnumUtility {
    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, String value) {
        if (enumClass == null || value == null) {
            throw new IllegalArgumentException("Enum class and value must not be null");
        }

        String formattedValue = value.toUpperCase(Locale.ROOT);
        for (T enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equals(formattedValue)) {
                return enumConstant;
            }
        }

        throw new IllegalArgumentException(
                "No enum constant " + enumClass.getCanonicalName() + " matches value: " + value);
    }
}
