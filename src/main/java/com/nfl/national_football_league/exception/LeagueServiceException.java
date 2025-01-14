package com.nfl.national_football_league.exception;

public class LeagueServiceException extends CustomGenericException{
    public LeagueServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}