package com.nfl.national_football_league.constant;

import com.google.api.client.http.HttpStatusCodes;
import org.apache.http.HttpStatus;

public class RestResponseStatusCode {
    public static final int VALIDATION_ERROR_STATUS = HttpStatusCodes.STATUS_CODE_BAD_REQUEST;
    public static final int INTERNAL_ERROR_STATUS = HttpStatusCodes.STATUS_CODE_SERVER_ERROR;
    public static final int NOT_FOUND_STATUS = HttpStatusCodes.STATUS_CODE_NOT_FOUND;
    public static final int NOT_ALLOWED_STATUS = HttpStatusCodes.STATUS_CODE_UNPROCESSABLE_ENTITY;
    public static final int NO_ACCESS_STATUS = HttpStatusCodes.STATUS_CODE_UNAUTHORIZED;
    public static final int OK_STATUS = HttpStatusCodes.STATUS_CODE_OK;
    public static final int REQUEST_TIMEOUT = HttpStatus.SC_REQUEST_TIMEOUT;
    public static final int ALREADY_EXISTS_STATUS = HttpStatusCodes.STATUS_CODE_CONFLICT;
}
