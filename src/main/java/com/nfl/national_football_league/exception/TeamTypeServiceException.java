package com.nfl.national_football_league.exception;

import lombok.Getter;

@Getter
public class TeamTypeServiceException extends CustomGenericException{
    public TeamTypeServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}
