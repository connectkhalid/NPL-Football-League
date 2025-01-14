package com.nfl.national_football_league.api.prv;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.SeasonServiceException;
import com.nfl.national_football_league.exception.TournamentServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import com.nfl.national_football_league.service.TournamentService;
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

import java.time.Year;
import java.util.List;

import static com.nfl.national_football_league.constant.RestApiResponse.buildResponseWithDetails;

@Slf4j
@RestController
@CrossOrigin
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TournamentController {
    public static final String API_PATH_TOURNAMENT_REGISTER = Constants.ApiPath.PRIVATE_API_PATH + "/register-tournament";
    public static final String API_PATH_GET_TOURNAMENT_LIST = Constants.ApiPath.PRIVATE_API_PATH + "/get-tournament-list";
    public static final String API_PATH_TEAM_REGISTER_TOURNAMENT_SEASON = Constants.ApiPath.PRIVATE_API_PATH + "/register-team-tournament-season";
    public static final String API_PATH_TOURNAMENT_SEASON_TEAM_LIST = Constants.ApiPath.PRIVATE_API_PATH + "/get-team-list-tournament-season";
    public static final String API_PATH_GET_TOURNAMENT_SEASON_TEAM_LIST = Constants.ApiPath.PRIVATE_API_PATH + "/get-tournament-season-list";


    private final TournamentService tournamentService;

    @Data
    public static class TournamentRegisterInputParameter {
        @NotBlank(message = "tournamentName field is mandatory")
        String tournamentName;
        @NotBlank(message = "tournamentOwner field is mandatory")
        String tournamentOwner;
        @NotBlank(message = "email field is mandatory")
        String email;
        @NotBlank(message = "phoneNumber field is mandatory")
        String phoneNumber;
        @NotBlank(message = "address field is mandatory")
        String address;
        @NotBlank(message = "imagePath field is mandatory")
        String imagePath;
        @NotNull(message = "tournamentYear field is mandatory")
        int tournamentYear;
        @NotBlank(message = "tournamentStartTime field is mandatory")
        String tournamentStartTime;
        @NotBlank(message = "tournamentEndTime field is mandatory")
        String tournamentEndTime;
    }

    @Data
    public static class TeamTournamentRegisterInputParameter {
        @NotNull(message = "fkTeamInfoId field is mandatory")
        long fkTeamInfoId;
        @NotNull(message = "tournamentInfoId field is mandatory")
        long tournamentInfoId;
        @NotNull(message = "fkSeasonInfoId field is mandatory")
        long fkSeasonInfoId;
    }

    @Data
    public static class TournamentIdSeasonIdInputParameter {
        @NotNull(message = "tournamentId field is mandatory")
        long tournamentId;
        @NotNull(message = "seasonId field is mandatory")
        long seasonId;
    }

    @PreAuthorize("hasAuthority(@apiPermission.tournamentCreatePermission.code)")
    @PostMapping(path = API_PATH_TOURNAMENT_REGISTER,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> tournamentRegister(@Valid @RequestBody TournamentRegisterInputParameter registerInput)
            throws AuthServiceException,  TournamentServiceException {

        TournamentService.TournamentRegisterInputParameter parameter = new TournamentService.TournamentRegisterInputParameter(
                registerInput.getTournamentName(),
                registerInput.getTournamentOwner(),
                registerInput.getEmail(),
                registerInput.getPhoneNumber(),
                registerInput.getAddress(),
                registerInput.getImagePath(),
                Year.of(registerInput.getTournamentYear()),
                registerInput.getTournamentStartTime(),
                registerInput.getTournamentEndTime()
        );

        TournamentService.TournamentRegisterResponse tournamentRegisterResponse = tournamentService.registerTournament(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, tournamentRegisterResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.tournamentListViewPermission.code)")
    @GetMapping(path = API_PATH_GET_TOURNAMENT_LIST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getTournamentList(){
        List<TournamentService.TournamentDataResponse> toTournamentDataResponse = tournamentService.getTournamentList();
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toTournamentDataResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.tournamentCreatePermission.code)")
    @PostMapping(path = API_PATH_TEAM_REGISTER_TOURNAMENT_SEASON,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> registerTeamToTournamentSeason(@Valid @RequestBody TeamTournamentRegisterInputParameter parameter)
            throws TournamentServiceException, AuthServiceException, SeasonServiceException, TeamServiceException {
        TournamentService.TeamTournamentRegisterInputParameter teamTournamentRegisterInputParameter = new TournamentService.
                TeamTournamentRegisterInputParameter(
                parameter.fkTeamInfoId,
                parameter.tournamentInfoId,
                parameter.fkSeasonInfoId);
        TournamentService.TeamListTournamentSeasonResponse teamLeagueResponse =  tournamentService.registerTeamToTournament(teamTournamentRegisterInputParameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, teamLeagueResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.tournamentCreatePermission.code)")
    @GetMapping(path = API_PATH_TOURNAMENT_SEASON_TEAM_LIST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> teamLeagueRegister(@Valid @RequestBody TournamentIdSeasonIdInputParameter parameter)
            throws TournamentServiceException, SeasonServiceException {
        TournamentService.TeamListTournamentSeasonResponse response = tournamentService.getTeamListByTournamentSeason(parameter.tournamentId, parameter.seasonId);

        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, response);
    }
}
