package com.nfl.national_football_league.api.prv;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.constant.SeasonCategory;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.ClubServiceException;
import com.nfl.national_football_league.exception.SeasonServiceException;
import com.nfl.national_football_league.exception.TournamentServiceException;
import com.nfl.national_football_league.form.annotation.DateFormat;
import com.nfl.national_football_league.service.SeasonService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;

import static com.nfl.national_football_league.constant.RestApiResponse.buildResponseWithDetails;

@Slf4j
@RestController
@CrossOrigin
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SeasonController {
    public static final String API_PATH_SEASON_REGISTER = Constants.ApiPath.PRIVATE_API_PATH + "/register-season";

    private final SeasonService seasonService;

    @Data
    public static class SeasonRegisterInputParameter{
        @NotNull(message = "field is mandatory")
        SeasonCategory seasonCategory;

        @NotNull(message = "SeasonCategory field is mandatory")
        String categoryName;

        @NotNull(message = "Field is mandatory")
        String location;

        @NotNull(message = "Field is mandatory")
        int seasonYear;

        @NotBlank(message = "field is mandatory")
        @DateFormat(message = "invalid Date format")
        String startDate;

        @NotBlank(message = "field is mandatory")
        @DateFormat(message = "invalid Date format")
        String endDate;
    }

    @PreAuthorize("hasAuthority(@apiPermission.teamCreatePermission.code)")
    @PostMapping(path = API_PATH_SEASON_REGISTER,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
   public ResponseEntity<Object> seasonRegister(@Valid @RequestBody SeasonController.SeasonRegisterInputParameter seasonRegisterInput)
            throws SeasonServiceException, AuthServiceException, ClubServiceException, TournamentServiceException {
        SeasonService.SeasonRegisterInputParameter parameter = new SeasonService.SeasonRegisterInputParameter(
                seasonRegisterInput.getSeasonCategory(),
                seasonRegisterInput.getCategoryName(),
                seasonRegisterInput.getLocation(),
                Year.of(seasonRegisterInput.getSeasonYear()),
                seasonRegisterInput.getStartDate(),
                seasonRegisterInput.getEndDate()
        );

        SeasonService.SeasonDataResponse seasonDataResponse = seasonService.registerSeason(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, seasonDataResponse);
   }
}
