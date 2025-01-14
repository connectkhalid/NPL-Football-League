package com.nfl.national_football_league.exception;

import com.nfl.national_football_league.constant.RestResponseStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

import static com.nfl.national_football_league.constant.RestApiResponse.buildResponseWithError;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            AccountServiceException.class, AuthServiceException.class, FileUploadException.class,
            PlayerServiceException.class, RoleServiceException.class, SeasonServiceException.class,
            TeamServiceException.class,TeamTypeServiceException.class, ClubServiceException.class,
            LeagueServiceException.class,TournamentServiceException.class
    })
    public ResponseEntity<Object> handleCustomExceptions(CustomGenericException ex) {
        return buildResponseWithError(ex.getErrorMessage(), ex.getHttpStatus(), ex.getErrorDetail());
    }

    @ExceptionHandler({
            NullPointerException.class, NumberFormatException.class, IllegalArgumentException.class,
            IllegalStateException.class, RuntimeException.class, ClassCastException.class,
            ClassNotFoundException.class, OutOfMemoryError.class, StackOverflowError.class,
            IOException.class, SQLException.class, BadCredentialsException.class, IllegalStateException.class
    })
    public ResponseEntity<Object> handleCommonExceptions(Exception ex) {
        return buildResponseWithError(
                "An error occurred while processing your request.",
                RestResponseStatusCode.INTERNAL_ERROR_STATUS,
                ex.getClass().getSimpleName() + ": " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownException(Exception ex) {
        return buildResponseWithError(
                "An unknown error occurred.",
                RestResponseStatusCode.INTERNAL_ERROR_STATUS,
                ex.getClass().getSimpleName() + ": " + ex.getMessage());
    }
}