package com.nfl.national_football_league.exception;

public class TournamentServiceException extends CustomGenericException{
    public TournamentServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}
