package com.nfl.national_football_league.exception;

import lombok.Getter;

public class PlayerServiceException extends CustomGenericException{
    public PlayerServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}
