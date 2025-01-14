package com.nfl.national_football_league.exception;

public class RoleServiceException extends CustomGenericException{
    public RoleServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}