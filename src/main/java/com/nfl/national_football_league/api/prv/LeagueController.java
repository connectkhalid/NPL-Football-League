package com.nfl.national_football_league.api.prv;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.LeagueServiceException;
import com.nfl.national_football_league.exception.SeasonServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import com.nfl.national_football_league.service.LeagueService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nfl.national_football_league.constant.RestApiResponse.buildResponseWithDetails;

@Slf4j
@RestController
@CrossOrigin
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LeagueController {
    public static final String API_PATH_LEAGUE_REGISTER = Constants.ApiPath.PRIVATE_API_PATH + "/register-league";
    public static final String API_PATH_LEAGUE_UPDATE = Constants.ApiPath.PRIVATE_API_PATH + "/update-league";
    public static final String API_PATH_GET_LEAGUE_LIST = Constants.ApiPath.PRIVATE_API_PATH + "/get-league-list";
    public static final String API_PATH_TEAM_LEAGUE_REGISTER = Constants.ApiPath.PRIVATE_API_PATH + "/register-team-league";
    public static final String API_PATH_LEAGUE_TEAM_LIST = Constants.ApiPath.PRIVATE_API_PATH + "/get-team-list-league";

    private final LeagueService leagueService;

    @Data
    public static class LeagueInputParameter {
        long leagueId;
        @NotBlank(message = "field is mandatory")
        String leagueName;
        @NotBlank(message = "field is mandatory")
        String leagueOwner;
        @NotBlank(message = "field is mandatory")
        String email;
        @NotBlank(message = "field is mandatory")
        String phoneNumber;
        @NotBlank(message = "field is mandatory")
        String address;
        @NotNull(message = "field is mandatory")
        int tire;
        String imagePath;
    }

    @Data
    public static class TeamLeagueRegisterInputParameter{
        @NotNull(message = "field is mandatory")
        long fkTeamInfoId;
        @NotNull(message = "field is mandatory")
        long fkSeasonInfoId;
        @NotNull(message = "field is mandatory")
        long fkLeagueInfoId;
    }

    @Data
    public static class LeagueIdInputParameter {
        @NotNull(message = "leagueId field is mandatory")
        long leagueId;
        @NotNull(message = "fkSeasonId field is mandatory")
        long fkSeasonId;
    }

    @PreAuthorize("hasAuthority(@apiPermission.leagueCreatePermission.code)")
    @PostMapping(path = API_PATH_LEAGUE_REGISTER,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> leagueRegister(@Valid @RequestBody LeagueController.LeagueInputParameter registerInput) throws AuthServiceException, TeamServiceException, LeagueServiceException {

        LeagueService.LeagueInputParameter parameter = new LeagueService.LeagueInputParameter(
                registerInput.getLeagueName(),
                registerInput.getLeagueOwner(),
                registerInput.getEmail(),
                registerInput.getPhoneNumber(),
                registerInput.getAddress(),
                registerInput.getTire()
        );

        LeagueService.LeagueRegisterResponse leagueRegisterResponse = leagueService.registerLeague(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, leagueRegisterResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.leagueUpdatePermission.code)")
    @PutMapping(path = API_PATH_LEAGUE_UPDATE,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> leagueUpdate(@Valid @RequestBody LeagueController.LeagueInputParameter registerInput) throws AuthServiceException, LeagueServiceException {

        LeagueService.LeagueInputParameter parameter = new LeagueService.LeagueInputParameter(
                registerInput.getLeagueId(),
                registerInput.getLeagueName(),
                registerInput.getLeagueOwner(),
                registerInput.getEmail(),
                registerInput.getPhoneNumber(),
                registerInput.getAddress(),
                registerInput.getTire(),
                registerInput.getImagePath()
        );

        LeagueService.LeagueRegisterResponse leagueRegisterResponse = leagueService.updateLeague(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, leagueRegisterResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.leagueListViewPermission.code)")
    @GetMapping(path = API_PATH_GET_LEAGUE_LIST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getDeviceInfo() throws TeamServiceException {
        List<LeagueService.LeagueDataResponse> toLeagueDataResponse = leagueService.getLeagueList();
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toLeagueDataResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.teamCreatePermission.code)")
    @PostMapping(path = API_PATH_TEAM_LEAGUE_REGISTER,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getTeamListLeague(@Valid @RequestBody TeamLeagueRegisterInputParameter parameter)
            throws LeagueServiceException, AuthServiceException, SeasonServiceException {
        LeagueService.TeamLeagueRegisterInputParameter teamLeagueRegisterInputParameter = new LeagueService.
                TeamLeagueRegisterInputParameter(parameter.fkTeamInfoId, parameter.fkSeasonInfoId, parameter.fkLeagueInfoId);
        LeagueService.LeagueDataResponse teamLeagueResponse =  leagueService.registerTeamToLeague(teamLeagueRegisterInputParameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, teamLeagueResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.teamCreatePermission.code)")
    @GetMapping(path = API_PATH_LEAGUE_TEAM_LIST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> teamLeagueRegister(@Valid @RequestBody LeagueIdInputParameter parameter)
            throws LeagueServiceException, TeamServiceException, SeasonServiceException {
        LeagueService.LeagueDataResponse response = leagueService.getTeamListLeague(parameter.leagueId, parameter.fkSeasonId);

        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, response);
    }
}
