package com.nfl.national_football_league.exception;

import org.springframework.http.HttpStatus;

public class AccountServiceException extends CustomGenericException{
    public AccountServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}
