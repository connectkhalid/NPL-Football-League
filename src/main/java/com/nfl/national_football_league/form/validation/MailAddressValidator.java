package com.nfl.national_football_league.form.validation;

import com.nfl.national_football_league.form.annotation.MailAddress;
import com.nfl.national_football_league.util.ParameterCheckInDatabaseUtil;
import com.nfl.national_football_league.util.ParameterCheckUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@Slf4j
public class MailAddressValidator implements ConstraintValidator<MailAddress, String> {
    @Autowired ParameterCheckUtil parameterCheckUtil;
    @Autowired ParameterCheckInDatabaseUtil parameterCheckInDatabaseUtil;

    @Override
    public boolean isValid(String mailAddress, ConstraintValidatorContext cvc) {
        log.info("START isValid");
        if (! StringUtils.hasText(mailAddress)) {
            log.info("END isValid parameter is empty");
            return true;
        }

        boolean result = parameterCheckUtil.checkMail(mailAddress);
        if(result){
            result = parameterCheckInDatabaseUtil.checkMailinDatabase(mailAddress);
        }
        log.info("END isValid. result:{}", result);
        return result;
    }
}
