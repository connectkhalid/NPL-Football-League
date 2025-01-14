package com.nfl.national_football_league.constant;

import org.springframework.security.core.parameters.P;

public class RestResponseMessage {
    public static final String VALIDATION_ERROR = "WARN_INPUT_PARAM";
    public static final String SQL_ERROR = "DATABASE_ERROR";
    public static final String REQUEST_ERROR = "ERR_REQUEST";

    public static final String UNKNOWN_ERROR = "ERR_UNKNOWN";
    public static final String TIMEOUT_ERROR = "REQUEST_TIMEOUT_ERROR";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    // Entity Not Found
    public static final String USER_NOT_FOUND = "INFO_NO_USER";
    public static final String ROLE_NOT_FOUND = "INFO_NO_ROLE";
    public static final String AUTH_DATA_NOT_FOUND = "INFO_NO_AUTH_DATA";

    // Operation Success
    public static final String CREATE_OK = "SUCCESSFULLY_CREATED";
    public static final String UPDATE_OK = "SUCCESSFULLY_UPDATED";
    public static final String DELETE_OK = "SUCCESSFULLY_DELETED";
    public static final String FETCH_OK = "SUCCESSFULLY_FETCHED";
    public static final String SEND_MAIL_OK = "SUCCESSFULLY_SEND_MAIL";
    public static final String FILE_UPLOAD_OK = "SUCCESSFULLY_UPLOAD_FILE";

    // User Service
    public static final String USER_ALREADY_REGISTERED = "USER_ALREADY_REGISTERED";

    // Auth Service
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String EXPIRED_TOKEN = "EXPIRED_TOKEN";
    public static final String WRONG_CREDENTIALS = "WRONG_CREDENTIALS";
    public static final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String REFRESH_SUCCESS = "REFRESH_SUCCESS";
    public static final String LOGOUT_SUCCESS = "LOGOUT_SUCCESS";
    public static final String NOT_LOGGED_IN = "NOT_LOGGED_IN";

    public static final String ACCESS_DENIED = "Access is denied";
    public static final String TIMEOUT_MESSAGE = "transaction timeout expired";

    public static final String SEND_MAIL_FAILURE = "MAIL_SENDING_FAILED";

    public static final String ROLE_MISMATCH = "USER_ROLE_CHANGED";

    public static final String PLAYER_NOT_FOUND = "INFO_NO_PLAYER";

    public static final String TEAM_NOT_FOUND = "INFO_NO_TEAM";

    public static final String FILE_UPLOAD_FAILURE = "FILE_UPLOAD_FAILED";

    public static final String PLAYER_TEAM_INFO_EXIST = "PLAYER_ALREADY_ASSIGNED_TO_TEAM";
    public static final String PLAYER_TEAM_INFO_ALREADY_EXIST = "PLAYER_ALREADY_EXISTS_TO__TEAM";


    public static final String PLAYER_TEAM_INFO_EXIST_ERROR_MESSAGE = "PLAYER_TEAM_INFO_EXIST_WITH_GIVEN_TEAMID_AND_PLAYERID";

    public static final String CLUB_ALREADY_EXISTS = "CLUB_ALREADY_EXISTS";
    public static final String LEAGUE_ALREADY_EXISTS = "LEAGUE_ALREADY_EXISTS";
    public static final String TOURNAMENT_ALREADY_EXISTS = "TOURNAMENT_ALREADY_EXISTS";
    public static final String NO_PLAYER_ASSIGNED_IN_TEAM = "NO_PLAYER_ASSIGNED_IN_TEAM";
    public static final String LEAGUE_NOT_FOUND = "INFO_NO_LEAGUE";
    public static final String CLUB_NOT_FOUND = "INFO_NO_CLUB";
    public static final String TEAM_LEAGUE_INFO_ALREADY_EXISTS = "INFO_TEAM_LEAGUE_ALREADY_EXISTS";
    public static final String NO_TEAM_LEAGUE_INFO_FOUND = "INFO_TEAM_LEAGUE_NOT_FOUND";
    public static final String SESSION_INFO_ALREADY_EXISTS = "INFO_SEASON_ALREADY_EXISTS";
    public static final String TEAM_ALREADY_EXISTS = "INFO_TEAM_ALREADY_EXISTS";
    public static final String TOURNAMENT_INFO_NOT_FOUND = "INFO_TOURNAMENT_NOT_FOUND";
    public static final String TEAM_ALREADY_EXISTS_IN_CLUB = "INFO_TEAM_ALREADY_EXISTS_IN_CLUB";
    public static final String TEAM_ALREADY_EXISTS_IN_LEAGUE = "INFO_TEAM_ALREADY_EXISTS_IN_LEAGUE";
    public static final String TEAM_ALREADY_EXISTS_IN_TOURNAMENT_SEASON = "INFO_TEAM_ALREADY_EXISTS_IN_TOURNAMENT_SEASON";
    public static final String SEASON_INFO_NOT_FOUND = "INFO_SEASON_NOT_FOUND";
    public static final String INFO_PLAYER_ALREADY_EXISTS = "INFO_PLAYER_ALREADY_EXISTS";
}
