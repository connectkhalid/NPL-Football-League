package com.nfl.national_football_league.api.prv;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.PlayerServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import com.nfl.national_football_league.service.PlayerService;
import com.nfl.national_football_league.service.TeamService;
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
public class TeamController {
    public static final String API_PATH_TEAM_REGISTER = Constants.ApiPath.PRIVATE_API_PATH + "/register-team";
    public static final String API_PATH_TEAM_UPDATE = Constants.ApiPath.PRIVATE_API_PATH + "/update-team";
    public static final String API_PATH_GET_TEAM_LIST = Constants.ApiPath.PRIVATE_API_PATH + "/get-team-list";
    public static final String API_PATH_GET_TEAM_LIST_BY_TEAM_CATEGORY = Constants.ApiPath.PRIVATE_API_PATH + "/get-team-list-by-team-category";
    public static final String API_PATH_PLAYER_TEAM_REGISTER = Constants.ApiPath.PRIVATE_API_PATH + "/register-player-team";
    public static final String API_PATH_GET_PLAYER_LIST_BY_TEAM = Constants.ApiPath.PRIVATE_API_PATH + "/get-player-list-by-team";

    private final TeamService teamService;
    @Data
    public static class TeamInputParameter {
        long teamId;
        @NotBlank(message = "teamName field is mandatory")
        String teamName;
        @NotBlank(message = "teamCategory is mandatory")
        String teamCategory;
        @NotBlank(message = "teamOwner field is mandatory")
        String teamOwner;
        @NotBlank(message = "email field is mandatory")
        String email;
        @NotBlank(message = "phoneNumber field is mandatory")
        String phoneNumber;
        @NotBlank(message = "address field is mandatory")
        String address;
        @NotNull(message = "tire field is mandatory")
        int tire;
        String imagePath;
    }

    @Data
    public static class InputTeamCategory {
        @NotBlank(message = "teamCategory field is required")
        String teamCategory;
    }

    @Data
    public static class PlayerTeamRegisterInputParameter{
        @NotNull(message = "field is mandatory")
        long fkPlayerInfoId;
        @NotNull(message = "field is mandatory")
        long fkTeamInfoId;
    }

    @Data
    public static class TeamIdInputParameter {
        @NotNull(message = "field is mandatory")
        long teamId;
    }

    @PreAuthorize("hasAuthority(@apiPermission.teamCreatePermission.code)")
    @PostMapping(path = API_PATH_TEAM_REGISTER,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> teamRegister(@Valid @RequestBody TeamController.TeamInputParameter registerInput) throws  AuthServiceException, TeamServiceException {
        TeamService.TeamInfoInputParameter parameter = new TeamService.TeamInfoInputParameter(
                registerInput.getTeamName(),
                registerInput.getTeamCategory(),
                registerInput.getTeamOwner(),
                registerInput.getEmail(),
                registerInput.getPhoneNumber(),
                registerInput.getAddress(),
                registerInput.getTire()
        );

        TeamService.TeamDataResponse teamRegisterResponse = teamService.registerTeam(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, teamRegisterResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.teamCreatePermission.code)")
    @PutMapping(path = API_PATH_TEAM_UPDATE,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> teamUpdate(@Valid @RequestBody TeamController.TeamInputParameter registerInput) throws  AuthServiceException, TeamServiceException {
        TeamService.TeamInfoInputParameter parameter = new TeamService.TeamInfoInputParameter(
                registerInput.getTeamId(),
                registerInput.getTeamName(),
                registerInput.getTeamCategory(),
                registerInput.getTeamOwner(),
                registerInput.getEmail(),
                registerInput.getPhoneNumber(),
                registerInput.getAddress(),
                registerInput.getTire(),
                registerInput.getImagePath()
        );

        TeamService.TeamDataResponse teamRegisterResponse = teamService.updateTeam(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, teamRegisterResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.teamListViewPermission.code)")
    @GetMapping(path = API_PATH_GET_TEAM_LIST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getAllTeamList() throws TeamServiceException {
        List<TeamService.TeamDataResponse> toTeamDataResponse = teamService.getTeamList();
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toTeamDataResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.teamListViewPermission.code)")
    @GetMapping(path = API_PATH_GET_TEAM_LIST_BY_TEAM_CATEGORY,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getAllTeamListByTeamCategory(@Valid @RequestBody InputTeamCategory inputTeamCategory) throws TeamServiceException {
        TeamService.InputTeamCategory parameter = new TeamService.InputTeamCategory(inputTeamCategory.getTeamCategory());
        List<TeamService.TeamDataResponse> toTeamDataResponse = teamService.getTeamListByCategory(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toTeamDataResponse);
    }


    @PreAuthorize("hasAuthority(@apiPermission.teamCreatePermission.code)")
    @PostMapping(path = API_PATH_PLAYER_TEAM_REGISTER,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> playerTeamRegister(@Valid @RequestBody PlayerTeamRegisterInputParameter playerTeamRegisterInput) throws PlayerServiceException, TeamServiceException {

        TeamService.PlayerTeamRegisterInputParameter parameter = new TeamService.PlayerTeamRegisterInputParameter(
                playerTeamRegisterInput.getFkPlayerInfoId(),
                playerTeamRegisterInput.getFkTeamInfoId()
        );

        TeamService.TeamDataResponse playerTeamRegisterResponse = teamService.registerPlayerByTeam(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, playerTeamRegisterResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.teamListViewPermission.code)")
    @GetMapping(path = API_PATH_GET_PLAYER_LIST_BY_TEAM,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getAllTeamPlayerList(@RequestBody TeamIdInputParameter parameter) throws  TeamServiceException {
        List<PlayerService.PlayerInfoResponse> toTeamDataResponse = teamService.getTeamPlayerListByTeamId(parameter.getTeamId());
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toTeamDataResponse);
    }
}
