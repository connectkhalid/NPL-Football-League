package com.nfl.national_football_league.service;

import com.nfl.national_football_league.exception.AuthServiceException;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface JwtLoginService {

    @Builder
    @Data
    class ExpiredTokenResponse {
        private String accessKey;
        private String mailAddress;
        private Long userId;
    }

    @Builder
    @Data
    class LoginResponse {
        private Long userId;
        private long roleId;
        private long teamId;
        private String accessKey;
        private String firstName;
        private String lastName;
        private Timestamp joiningDt;
        private String mailAddress;
        private String password;
        private Boolean deleteFlg;
        private long createdUserId;


    }

    @Builder
    @Data
    class RefreshTokenResponse {
        private Long userId;
        private long roleId;
        private long teamId;
        private String accessKey;
        private String firstName;
        private String lastName;
        private Timestamp joiningDt;
        private String mailAddress;
        private String password;
        private Boolean deleteFlg;
        private long createdUserId;
    }

    LoginResponse loginWithJwtSecurity(String mailAddress, String password) throws AuthServiceException;

   // @Transactional(rollbackFor = Exception.class)
    RefreshTokenResponse getRefreshToken() throws AuthServiceException;

    @Transactional(rollbackFor = Exception.class)
    void logoutWithJwtSecurity() throws AuthServiceException;
}
