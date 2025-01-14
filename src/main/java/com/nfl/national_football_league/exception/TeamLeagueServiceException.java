package com.nfl.national_football_league.exception;

public class TeamLeagueServiceException extends CustomGenericException {
    public TeamLeagueServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}
