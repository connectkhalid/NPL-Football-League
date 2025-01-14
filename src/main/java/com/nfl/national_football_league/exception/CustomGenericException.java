package com.nfl.national_football_league.exception;

public class CustomGenericException extends Exception {
    private final String errorMessage;
    private int httpStatus = 400;
    private final String errorDetail;

    public CustomGenericException(String errorMessage, int httpStatus, String errorDetail) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }
    public CustomGenericException(String errorMessage, String errorDetail) {
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorDetail() {
        return errorDetail;
    }
}
