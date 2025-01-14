package com.nfl.national_football_league.service;

import com.nfl.national_football_league.domain.AccountInfo;
import com.nfl.national_football_league.exception.AccountServiceException;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.RoleServiceException;
import com.nfl.national_football_league.form.annotation.MailAddress;
import com.nfl.national_football_league.form.annotation.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountService {

    @Builder
    @Data
    class AccountRegisterResponse{
        private long adminId;
        private String firstName;
        private String lastName;
        private String mailAddress;
        private String password;
        private String phoneNumber;
        private String location;
        private String roleName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AccountRegisterInputParameter{
        private String firstName;
        private String lastName;
        private String mailAddress;
        private String password;
        private String phoneNumber;
        private String location;
        private long roleCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AccountUpdateInputParameter{
        private long adminId;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String location;
    }

    @Transactional(rollbackFor = Exception.class)
    AccountRegisterResponse registerAccount(AccountRegisterInputParameter parameter) throws AccountServiceException, RoleServiceException;
    AccountInfo getAccountInformationByEmail(String mailAddress) throws AccountServiceException, AuthServiceException;

    List<AccountRegisterResponse> getAdminList() throws AccountServiceException;

    AccountRegisterResponse getAdminInfo(long adminId) throws AccountServiceException, AuthServiceException;

    AccountRegisterResponse updateAdminInfo(AccountUpdateInputParameter parameter) throws AccountServiceException, AuthServiceException;
}
