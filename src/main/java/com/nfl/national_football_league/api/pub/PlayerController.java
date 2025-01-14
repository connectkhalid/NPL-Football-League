package com.nfl.national_football_league.api.pub;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.exception.*;
import com.nfl.national_football_league.form.annotation.*;
import com.nfl.national_football_league.service.PlayerService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.nfl.national_football_league.constant.RestApiResponse.buildResponseWithDetails;

@Slf4j
@RestController
@CrossOrigin
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerController {
    public static final String API_PATH_PLAYER_REGISTER = Constants.ApiPath.PUBLIC_API_PATH + "/register-player";
    public static final String API_PATH_GET_PLAYER_LIST = Constants.ApiPath.PRIVATE_API_PATH + "/get-player-list";
    public static final String API_PATH_GET_PLAYER_INFO = Constants.ApiPath.PRIVATE_API_PATH + "/get-player-info";
    public static final String API_PATH_UPDATE_PLAYER_INFO = Constants.ApiPath.PRIVATE_API_PATH + "/update-player-info";

    private final PlayerService playerService;
    @Data
    public static class PlayerRegisterInputParameter{
        @NotBlank(message = "field is mandatory")
        String firstName;

        @NotBlank(message = "field is mandatory")
        String lastName;

        @NotBlank(message = "field is mandatory")
        @MailAddress(message = "field value must be email")
        String mailAddress;

        @NotBlank(message = "field is mandatory")
        @Password(message = "invalid password format")
        String password;

        @NotBlank(message = "field is mandatory")
        //@PhoneNumber(message = "invalid Phone Number format")
        String phoneNumber;

        @NotBlank(message = "field is mandatory")
        @DateFormat(message = "invalid Date format")
        String dateOfBirth;

        @NotBlank(message = "field is mandatory")
        String height;

        @NotBlank(message = "field is mandatory")
        String weight;

        @NotBlank(message = "field is mandatory")
        String playingPosition;

        @NotNull(message = "field is mandatory")
        private long roleCode;

        String imagePath;

        @NotBlank(message = "field is mandatory")
        String nid;
    }

    @Data
    public static class InputPlayerId {
        @NotNull long playerId;
    }

    @Data
    public static class InputPlayerInfoUpdateParameter{
        @NotNull(message = "field is mandatory")
        long playerId;

        @NotBlank(message = "field is mandatory")
        String firstName;

        @NotBlank(message = "field is mandatory")
        String lastName;

        @NotBlank(message = "field is mandatory")
        //@PhoneNumber(message = "invalid Phone Number format")
        String phoneNumber;

        @NotBlank(message = "field is mandatory")
        @DateFormat(message = "invalid Date format")
        String dateOfBirth;

        @NotBlank(message = "field is mandatory")
        String height;

        @NotBlank(message = "field is mandatory")
        String weight;

        @NotBlank(message = "field is mandatory")
        String playingPosition;

        @NotBlank(message = "field is mandatory")
        String nid;

        private String imagePath;
    }

    @PostMapping(path = API_PATH_PLAYER_REGISTER,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> playerRegister(@Valid @RequestBody PlayerController.PlayerRegisterInputParameter playerRegisterInputParameter) throws PlayerServiceException, RoleServiceException {

        PlayerService.PlayerRegisterInputParameter parameter = new PlayerService.PlayerRegisterInputParameter(
                playerRegisterInputParameter.getFirstName(),
                playerRegisterInputParameter.getLastName(),
                playerRegisterInputParameter.getMailAddress(),
                playerRegisterInputParameter.getPassword(),
                playerRegisterInputParameter.getPhoneNumber(),
                playerRegisterInputParameter.getDateOfBirth(),
                playerRegisterInputParameter.getHeight(),
                playerRegisterInputParameter.getWeight(),
                playerRegisterInputParameter.getPlayingPosition(),
                playerRegisterInputParameter.getNid(),
                playerRegisterInputParameter.getRoleCode(),
                playerRegisterInputParameter.getImagePath()
        );

        PlayerService.PlayerRegisterResponse playerRegisterResponse = playerService.registerPlayer(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, playerRegisterResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.playerListViewPermission.code)")
    @GetMapping(
            path = API_PATH_GET_PLAYER_LIST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getPlayerList() throws PlayerServiceException {
        log.info("START execute.");
        List<PlayerService.PlayerInfoResponse> toPlayerDataResponse = playerService.getPlayerList();
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toPlayerDataResponse);
    }
    @PreAuthorize("hasAuthority(@apiPermission.playerProfileViewPermission.code)")
    @GetMapping(
            path = API_PATH_GET_PLAYER_INFO, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getPlayerInfo(InputPlayerId playerIdInput) throws PlayerServiceException, AuthServiceException {
        log.info("START execute.");
        PlayerService.PlayerInfoResponse playerInfoResponse = playerService.getPlayerInfo(playerIdInput.getPlayerId());
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, playerInfoResponse);
    }
    @PreAuthorize("hasAuthority(@apiPermission.playerProfileEditPermission.code)")
    @PutMapping(path = API_PATH_UPDATE_PLAYER_INFO,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> updatePlayerInfo(@RequestBody @Valid InputPlayerInfoUpdateParameter inputPlayerInfoUpdateParameter) throws PlayerServiceException, AuthServiceException {
        PlayerService.PlayerUpdateInputParameter parameter = new PlayerService.PlayerUpdateInputParameter(
                inputPlayerInfoUpdateParameter.getPlayerId(),
                inputPlayerInfoUpdateParameter.getFirstName(),
                inputPlayerInfoUpdateParameter.getLastName(),
                inputPlayerInfoUpdateParameter.getPhoneNumber(),
                inputPlayerInfoUpdateParameter.getDateOfBirth(),
                inputPlayerInfoUpdateParameter.getHeight(),
                inputPlayerInfoUpdateParameter.getWeight(),
                inputPlayerInfoUpdateParameter.getPlayingPosition(),
                inputPlayerInfoUpdateParameter.getNid(),
                inputPlayerInfoUpdateParameter.getImagePath()
        );
        PlayerService.PlayerInfoResponse playerInfoResponse = playerService.updatePlayerInfo(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.UPDATE_OK, playerInfoResponse);
    }

}
