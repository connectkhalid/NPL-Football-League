package com.nfl.national_football_league.api.pub;

import com.nfl.national_football_league.constant.Constants;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.exception.AccountServiceException;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.RoleServiceException;
import com.nfl.national_football_league.form.annotation.MailAddress;
import com.nfl.national_football_league.form.annotation.Password;
import com.nfl.national_football_league.service.AccountService;
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

import static com.nfl.national_football_league.constant.RestApiResponse.*;

@Slf4j
@RestController
@CrossOrigin
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController {
    public static final String API_PATH_ACCOUNT_REGISTER = Constants.ApiPath.PUBLIC_API_PATH + "/register-account";
    public static final String API_PATH_GET_ADMIN_LIST = Constants.ApiPath.PRIVATE_API_PATH + "/get-admin-list";
    public static final String API_PATH_GET_ADMIN_INFO = Constants.ApiPath.PRIVATE_API_PATH + "/get-admin-info";
    public static final String API_PATH_UPDATE_ADMIN_INFO = Constants.ApiPath.PRIVATE_API_PATH + "/update-admin-info";

    private final AccountService accountService;
    @Data
    public static class AccountRegisterInput {

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
        String phoneNumber;

        @NotBlank(message = "field is mandatory")
        String location;

        @NotNull(message = "field is mandatory")
        long roleCode;
    }

    @Data
    public static class InputAdminId {
        @NotNull long adminId;
    }

    @Data
    public static class InputAdminUpdate {

        @NotNull(message = "field is mandatory")
        long adminId;

        @NotBlank(message = "field is mandatory")
        String firstName;

        @NotBlank(message = "field is mandatory")
        String lastName;

        @NotBlank(message = "field is mandatory")
        String phoneNumber;

        @NotBlank(message = "field is mandatory")
        String location;
    }
    @PostMapping(path = API_PATH_ACCOUNT_REGISTER,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> accountRegister(@Valid @RequestBody AccountRegisterInput accountRegisterInput) throws AccountServiceException, RoleServiceException {
        AccountService.AccountRegisterInputParameter parameter = new AccountService.AccountRegisterInputParameter(
                accountRegisterInput.getFirstName(),
                accountRegisterInput.getLastName(),
                accountRegisterInput.getMailAddress(),
                accountRegisterInput.getPassword(),
                accountRegisterInput.getPhoneNumber(),
                accountRegisterInput.getLocation(),
                accountRegisterInput.getRoleCode()

        );

        AccountService.AccountRegisterResponse accountRegisterResponse = accountService.registerAccount(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.CREATE_OK, accountRegisterResponse);
    }
    @PreAuthorize("hasAuthority(@apiPermission.adminListViewPermission.code)")
    @GetMapping(
            path = API_PATH_GET_ADMIN_LIST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getAdminList() throws AccountServiceException{
        log.info("START execute.");
        List<AccountService.AccountRegisterResponse> toAccountDataResponse = accountService.getAdminList();
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toAccountDataResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.adminProfileViewPermission.code)")
    @GetMapping(
            path = API_PATH_GET_ADMIN_INFO, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getAdminInfo(InputAdminId adminIdInput) throws AccountServiceException, AuthServiceException {
        log.info("START execute.");
        AccountService.AccountRegisterResponse toAccountDataResponse = accountService.getAdminInfo(adminIdInput.getAdminId());
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.FETCH_OK, toAccountDataResponse);
    }

    @PreAuthorize("hasAuthority(@apiPermission.adminProfileEditPermission.code)")
    @PutMapping(path = API_PATH_UPDATE_ADMIN_INFO,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> updateAdminInfo(@RequestBody @Valid InputAdminUpdate inputAdminUpdateParameter) throws AccountServiceException, AuthServiceException {
        AccountService.AccountUpdateInputParameter parameter = new AccountService.AccountUpdateInputParameter(
                inputAdminUpdateParameter.getAdminId(),
                inputAdminUpdateParameter.getFirstName(),
                inputAdminUpdateParameter.getLastName(),
                inputAdminUpdateParameter.getPhoneNumber(),
                inputAdminUpdateParameter.getLocation()
        );
        AccountService.AccountRegisterResponse toAccountDataResponse = accountService.updateAdminInfo(parameter);
        return buildResponseWithDetails(RestResponseStatusCode.OK_STATUS, RestResponseMessage.UPDATE_OK, toAccountDataResponse);
    }

}
