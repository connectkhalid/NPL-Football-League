package com.nfl.national_football_league.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParameterCheckUtil {

    public boolean checkMail(String mail) {
        if (! StringUtils.hasText(mail)) {
            log.error("mail is empty.");
            return false;
        } else if (mail.length() <= 4 ||  mail.length() >= 257) {
            log.error("mail length invalid.");
            return false;
        }

        return EmailValidator.getInstance().isValid(mail);
    }

    public boolean checkPassword(String password) {
        if (! StringUtils.hasText(password)) {
            log.error("password is empty.");
            return false;
        }

        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[, ,!,%,&,@,#,$,^,*,?,_,~,(,),{},=,.,+,-]).{8,16}$");
        Matcher matcher = pattern.matcher(password);
        if (! matcher.matches()) {
            log.error("password invalid.");
            return false;
        }

        return true;
    }

    public boolean checkPhoneNumber(String phoneNumber) {
        if (! StringUtils.hasText(phoneNumber)) {
            log.error("Phone Number is empty.");
            return false;
        }

        Pattern pattern = Pattern.compile("[\\d-]{1,30}");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (! matcher.matches()) {
            log.error("Phone Number invalid.");
            return false;
        }

        return true;
    }

    public boolean checkDate(String dateAsString){

        if (! StringUtils.hasText(dateAsString)) {
            log.error("Date is empty.");
            return false;
        }

        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            simpleDateFormat.setLenient(false);

            String formatDateAsString = simpleDateFormat.format(simpleDateFormat.parse(dateAsString));
            if(!formatDateAsString.equals(dateAsString)){
                log.error("Date invalid.");
                return false;
            }
        } catch (Exception e){
            log.error("Date invalid.");
            return false;
        }

        return true;
    }
}
