package com.nfl.national_football_league.exception;

import lombok.Getter;

public class SeasonServiceException extends CustomGenericException{

    public SeasonServiceException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}