package com.nfl.national_football_league.api.prv;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.ClubServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import com.nfl.national_football_league.service.ClubService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class ClubController {
    public static final String API_PATH_CLUB_REGISTER = Constants.ApiPath.PRIVATE_API_PATH + "/register-club";
    public static final String API_PATH_CLUB_UPDATE = Constants.ApiPath.PRIVATE_API_PATH + "/update-club";
    public static final String API_PATH_GET_CLUB_LIST = Constants.ApiPath.PRIVATE_API_PATH + "/get-club-list";
    public static final String API_PATH_REGISTER_TEAM_TO_CLUB = Constants.ApiPath.PRIVATE_API_PATH + "/register-team-to-club";

    private final ClubService clubService;
    @Data
    public static class ClubInfoInputParameter{
        long clubId;
        @NotBlank(message = "clubName field is mandatory")
        String clubName;
        @NotBlank(message = "clubOwner field is mandatory")
        String clubOwner;
        @NotBlank(message = "email field is mandatory")
        String email;
        @NotBlank(message = "phoneNumber field is mandatory")
        String phoneNumber;
        @NotBlank(message = "address field is mandatory")
        String address;
        @NotBlank(message = "tire field is mandatory")
        String tire;
        String imagePath;

    }

    @Data
    public static class InputTeamType {
        @NotBlank String teamType;
    }

    @Data
    public static class InputAddTeamToClub {
        @NotBlank long fkTeamId;
        @NotBlank long clubId;
    }

    @PreAuthorize("hasAuthority(@apiPermission.clubCreatePermission.code)")
    @PostMapping(path = API_PATH_CLUB_REGISTER,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> clubRegister(@Valid @RequestBody ClubController.ClubInfoInputParameter clubRegisterInput) throws AuthServiceException, TeamServiceException, ClubServiceException {

        ClubService.ClubInfoInputParameter parameter = new ClubService.ClubInfoInputParameter(
                clubRegisterInput.getClubName(),
                clubRegisterInput.getClubOwner(),
                clubRegisterInput.getEmail(),
                clubRegisterInput.getPhoneNumber(),
                clubRegisterInput.getAddress(),
                clubRegisterInput.getTire()
        );

        ClubService.ClubDataResponse clubRegisterResponse = clubService.registerClub(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, clubRegisterResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.getClubUpdatePermission.code)")
    @PutMapping(path = API_PATH_CLUB_UPDATE,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> clubUpdate(@Valid @RequestBody ClubController.ClubInfoInputParameter clubRegisterInput) throws AuthServiceException, TeamServiceException, ClubServiceException {

        ClubService.ClubInfoInputParameter parameter = new ClubService.ClubInfoInputParameter(
                clubRegisterInput.getClubId(),
                clubRegisterInput.getClubName(),
                clubRegisterInput.getClubOwner(),
                clubRegisterInput.getEmail(),
                clubRegisterInput.getPhoneNumber(),
                clubRegisterInput.getAddress(),
                clubRegisterInput.getTire(),
                clubRegisterInput.getImagePath()
        );

        ClubService.ClubDataResponse clubUpdateResponse = clubService.updateClub(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, clubUpdateResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.clubListViewPermission.code)")
    @GetMapping(path = API_PATH_GET_CLUB_LIST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getDeviceInfo() throws TeamServiceException {
        List<ClubService.ClubDataResponse> toClubDataResponse = clubService.getClubList();
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toClubDataResponse);
    }

//    @PreAuthorize("hasAuthority(@apiPermission.getAddTeamToClubPermission.code)")
    @PostMapping(path = API_PATH_REGISTER_TEAM_TO_CLUB,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> registerTeamToClub( @RequestBody InputAddTeamToClub addTeamToClubInput) throws ClubServiceException, TeamServiceException {
        ClubService.ClubDataResponse toClubDataResponse = clubService.registerTeamToClub(new ClubService.TeamRegisterToClubInputParameter(addTeamToClubInput.clubId, addTeamToClubInput.fkTeamId));
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toClubDataResponse);
    }
}
