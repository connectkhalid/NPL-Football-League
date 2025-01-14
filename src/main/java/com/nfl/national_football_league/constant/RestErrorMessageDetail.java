package com.nfl.national_football_league.constant;

import org.apache.commons.beanutils.PropertyUtilsBean;

public class RestErrorMessageDetail {
    public static final String HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR_MESSAGE = "Sorry, the requested HTTP method is not supported.";
    public static final String HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR_MESSAGE = "Unsupported media type in the request.";
    public static final String HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_ERROR_MESSAGE = "None of the requested media types are acceptable.";
    public static final String CONVERSION_NOT_SUPPORTED_ERROR_MESSAGE = "Conversion not supported for the given data type.";
    public static final String HTTP_MESSAGE_NOT_READABLE_ERROR_MESSAGE = "Unable to read the HTTP message.";
    public static final String HTTP_MESSAGE_NOT_WRITABLE_ERROR_MESSAGE = "Unable to write the HTTP message.";
    public static final String NO_HANDLER_FOUND_ERROR_MESSAGE = "No handler found for the request.";
    public static final String ASYNC_REQUEST_TIMEOUT_ERROR_MESSAGE = "Async request timed out.";
    public static final String MISSING_PATH_VARIABLE_ERROR_MESSAGE = "Missing path variable in the request.";
    public static final String MISSING_SERVLET_REQUEST_PARAMETER_ERROR_MESSAGE = "Missing servlet request parameter.";
    public static final String TYPE_MISMATCH_ERROR_MESSAGE = "Type mismatch in request parameters.";
    public static final String MISSING_SERVLET_REQUEST_PART_ERROR_MESSAGE = "Missing servlet request part.";
    public static final String SERVLET_REQUEST_BINDING_ERROR_MESSAGE = "ServletRequest binding error.";
    public static final String METHOD_ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE = "Method argument type mismatch.";
    public static final String BINDING_ERROR_MESSAGE = "Can not bind input due to invalid format.";
    public static final String INVALID_FORMAT_ERROR_MESSAGE = "Invalid data format in the request.";
    public static final String MAX_UPLOAD_SIZE_EXCEEDED_ERROR_MESSAGE = "Uploaded file size exceeds the maximum limit.";
    public static final String BAD_CREDENTIALS_ERROR_MESSAGE = "Username or password is invalid.";
    public static final String SQL_ERROR_MESSAGE = "An error occurred while processing database.";
    public static final String STACK_OVERFLOW_ERROR_MESSAGE = "Please reduce the number of Wi-Fi settings or devices under this group.";
    public static final String TIMEOUT_ERROR_MESSAGE = "Request processing time exceeds the maximum limit.";
    public static final String NO_PERMISSION_ERROR_MESSAGE = "You do not have the permission to perform the task.";
    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown exception occurred, please check log for details.";
    public static final String INVALID_USER_TOKEN_ERROR_MESSAGE = "Token is not registered for a valid user.";
    public static final String INVALID_PLAN_TOKEN_ERROR_MESSAGE = "Token is not registered for a valid subscription plan.";
    public static final String INVALID_TOKEN_ERROR_MESSAGE = "Token is not valid.";
    public static final String EXPIRED_TOKEN_ERROR_MESSAGE = "Token has expired.";
    public static final String CORP_NOT_FOUND_ERROR_MESSAGE = "Corporation does not exist.";
    // Common
    public static final String GROUP_NOT_FOUND_ERROR_MESSAGE = "Group does not exist.";
    public static final String USER_NOT_FOUND_ERROR_MESSAGE = "User does not exist.";
    public static final String ROLE_NOT_FOUND_ERROR_MESSAGE = "Role does not exist.";

    public static final String ACCESS_KEY_NOT_FOUND_ERROR_MESSAGE = "Access key does not exist.";

    public static final String PLAYER_NOT_FOUND_ERROR_MESSAGE = "Player does not exist.";
    public static final String TEAM_NOT_FOUND_ERROR_MESSAGE = "Team does not exist.";
    public static final String TEAM_ALREADY_EXISTS_ERROR_MESSAGE = "Team info already exists with the given name";

    public static final String USER_ALREADY_LOGGED_OUT_ERROR_MESSAGE = "User Already logged out.";

    public static final String FILE_UPLOAD_FAILURE_ERROR_MESSAGE = "File upload failed.";

    public static final String CLUB_ALREADY_EXISTS_ERROR_MESSAGE = "Club already exists with the given information.";
    public static final String CLUB_NOT_FOUND_ERROR_MESSAGE = "Club info does not exists with the given information";
    public static final String LEAGUE_ALREADY_EXISTS_ERROR_MESSAGE = "League already exists with the given information.";
    public static final String TOURNAMENT_ALREADY_EXISTS_ERROR_MESSAGE = "Tournament already exists with the given information.";
    public static final String NO_PLAYER_ASSIGNED_IN_TEAM_ERROR_MESSAGE = "No player is assigned with the given teamId.";
    public static final String LEAGUE_NOT_FOUND_ERROR_MESSAGE = "No League find with the given information.";
    public static final String SEASON_ALREADY_EXISTS_ERROR_MESSAGE = "Season is already exist.";
    public static final String TOURNAMENT_INFO_NOT_FOUND_ERROR_DETAILS = "No Tournament info is found with the given information";

    public static final String TEAM_CLUB_ALREADY_EXISTS_ERROR_MESSAGE = "Team with given teamId already exists in the club";
    public static final String TEAM_LEAGUE_ALREADY_EXISTS_ERROR_MESSAGE = "Team with given teamId is already exists in the League.";
    public static final String TEAM_TOURNAMENT_SEASON_ALREADY_EXISTS_ERROR_MESSAGE = "Team with given teamId already exists the tournament season";
    public static final String SEASON_INFO_NOT_FOUND_ERROR_DETAILS = "No season Info Found with the given information";
    public static final String PLAYER_ALREADY_EXISTS_ERROR_DETAILS = "Player already exists with the given player details";
    public static final String PLAYER_TEAM_ALREADY_EXISTS_ERROR_DETAILS = "Player is already exists in a team";
    public static final String PLAYER_ALREADY_EXISTS_IN_TEAM_ERROR_DETAILS = "Player is already exists in this team";
}
