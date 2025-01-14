package com.nfl.national_football_league.form.validation;

import com.nfl.national_football_league.form.annotation.PhoneNumber;
import com.nfl.national_football_league.util.ParameterCheckUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Autowired ParameterCheckUtil parameterCheckUtil;

    @Override
    public void initialize(PhoneNumber phoneNumber) {

    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext cvc) {
        log.info("START isValid Phone Number:{}", phoneNumber);

        if (! parameterCheckUtil.checkPhoneNumber(phoneNumber)) {
            log.info("END isValid parameter is invalid.");
            return false;
        }
        log.info("END isValid success.");

        return false;
    }
}
