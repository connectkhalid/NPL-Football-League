package com.nfl.national_football_league.exception;

public class ClubServiceException extends CustomGenericException{
    public ClubServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}