package com.nfl.national_football_league.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class FileUploadException extends CustomGenericException {
    public FileUploadException(String errorMessage, int httpStatus, String errorDetail) {
        super(errorMessage, httpStatus, errorDetail);
    }
}