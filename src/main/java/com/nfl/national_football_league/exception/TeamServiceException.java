package com.nfl.national_football_league.exception;

public class TeamServiceException extends CustomGenericException{

    public TeamServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}