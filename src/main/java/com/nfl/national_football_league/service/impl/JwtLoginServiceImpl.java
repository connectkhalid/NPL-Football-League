package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.RestErrorMessageDetail;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.domain.AccessKeyInfo;
import com.nfl.national_football_league.domain.AccountInfo;
import com.nfl.national_football_league.domain.PlayerAccessKeyInfo;
import com.nfl.national_football_league.domain.PlayerInfo;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.repository.*;
import com.nfl.national_football_league.security.JwtTokenUtil;
import com.nfl.national_football_league.service.AuthCommonService;
import com.nfl.national_football_league.service.JwtLoginService;
import com.nfl.national_football_league.util.DateUtil;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtLoginServiceImpl implements JwtLoginService {

   private final AccountInfoRepository accountInfoRepository;
   private final PlayerInfoRepository playerInfoRepository;
   private final JwtTokenUtil jwtTokenUtil;
   private final AccessKeyInfoRepository accessKeyInfoRepository;
   private final PlayerAccesskeyInfoRepository playerAccesskeyInfoRepository;
   private final AuthenticationManager authenticationManager;
   private final RoleInfoRepository roleInfoRepository;
   private final AuthCommonService authCommonService;

    private final Clock clock = DefaultClock.INSTANCE;
    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;

    @Override
    public LoginResponse loginWithJwtSecurity(String mailAddress, String password) throws AuthServiceException {
        log.info("START login.");
        AccountInfo accountInfo = accountInfoRepository.findByMailAddressAndDeleteFlgIsFalse(mailAddress);
        PlayerInfo playerInfo = playerInfoRepository.findByMailAddressAndDeleteFlgIsFalse(mailAddress);

        if(Objects.isNull(accountInfo) && Objects.isNull(playerInfo)){
            log.info("account nothing.");
            throw new AuthServiceException(RestResponseMessage.WRONG_CREDENTIALS,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.BAD_CREDENTIALS_ERROR_MESSAGE);
        }

        checkPassword(mailAddress, password);

        return loginUser(accountInfo, playerInfo);
    }

    void checkPassword(String mailAddress, String password) throws AuthServiceException {
        try{
            authenticate(mailAddress, password);
        } catch (BadCredentialsException bcex){
            log.info("password invalid.");
            throw new BadCredentialsException(RestErrorMessageDetail.BAD_CREDENTIALS_ERROR_MESSAGE);
        } catch (Exception ex){
            log.warn("Error: {}", ex.getMessage());
            throw new AuthServiceException(RestResponseMessage.UNKNOWN_ERROR, RestResponseStatusCode.INTERNAL_ERROR_STATUS, RestErrorMessageDetail.UNKNOWN_ERROR_MESSAGE);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    LoginResponse loginUser(AccountInfo accountInfo, PlayerInfo playerInfo) throws AuthServiceException{
        if(!Objects.isNull(accountInfo) && (accountInfo.getRoleInfo().getRoleCode() == 1 || accountInfo.getRoleInfo().getRoleCode() == 2)){
            return getAdminLoginResponse(accountInfo);
        }else if(!Objects.isNull(playerInfo) && playerInfo.getRoleInfo().getRoleCode() == 3){
            return getPlayerLoginResponse(playerInfo);
        }

        log.info("account nothing.");
        throw new AuthServiceException(RestResponseMessage.WRONG_CREDENTIALS,
                RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.BAD_CREDENTIALS_ERROR_MESSAGE);
    }

    private LoginResponse getAdminLoginResponse(AccountInfo accountInfo) throws AuthServiceException {
        boolean lastLogin = Objects.isNull(accountInfo.getLastLoginDt());
        accountInfo.setLastLoginDt(DateUtil.currentTime());
        accountInfo = accountInfoRepository.save(accountInfo);
        setAccountDetails(accountInfo);

        log.info("delete expired Token");
        accessKeyInfoRepository.deleteByMailAddressAndExpDtLessThan(accountInfo.getMailAddress(), new Timestamp(clock.now().getTime() - (expiration * 1000)));

        final String token = jwtTokenUtil.generateToken(accountInfo.getMailAddress(), accountInfo.getRoleInfo().getRoleName());

        AccessKeyInfo accesskeyInfo = issueAccessKeyInfoForJwt(accountInfo.getId(), accountInfo.getMailAddress(), accountInfo.getRoleInfo().getRoleName());

        return LoginResponse.builder()
                .userId(accountInfo.getId())
                .roleId(accountInfo.getRoleInfoId())
                .accessKey(accesskeyInfo.getAccessKey())
                .firstName(accountInfo.getFirstName())
                .lastName(accountInfo.getLastName())
                .joiningDt(accountInfo.getJoiningDt())
                .mailAddress(accountInfo.getMailAddress())
                .password(accountInfo.getPassword())
                .deleteFlg(accountInfo.getDeleteFlg())
                .createdUserId(accountInfo.getCreatedUserId())
                .build();
    }

    private LoginResponse getPlayerLoginResponse(PlayerInfo playerInfo){
        boolean lastLogin = Objects.isNull(playerInfo.getLastLoginDt());
        playerInfo = playerInfoRepository.save(playerInfo);

        log.info("delete expired Token");
        playerAccesskeyInfoRepository.deleteByMailAddressAndExpDtLessThan(playerInfo.getMailAddress(), new Timestamp(clock.now().getTime() - (expiration * 1000)));

        final String token = jwtTokenUtil.generateToken(playerInfo.getMailAddress(), playerInfo.getRoleInfo().getRoleName());

        PlayerAccessKeyInfo playerAccessKeyInfo = issuePlayerAccessKeyInfoForJwt(playerInfo.getId(), playerInfo.getMailAddress(), playerInfo.getRoleInfo().getRoleName());

        return LoginResponse.builder()
                .userId(playerInfo.getId())
                .roleId(playerInfo.getRoleInfoId())
                .accessKey(playerAccessKeyInfo.getAccessKey())
                .firstName(playerInfo.getFirstName())
                .lastName(playerInfo.getLastName())
               // .joiningDt(playerInfo.getJoiningDt())
                .mailAddress(playerInfo.getMailAddress())
                .password(playerInfo.getPassword())
                .deleteFlg(playerInfo.getDeleteFlg())
               // .createdUserId(playerInfo.getCreatedUserId())
                .build();
    }

    private void setAccountDetails(AccountInfo accountInfo) throws AuthServiceException {
        accountInfo.setRoleInfo(Objects.isNull(accountInfo.getRoleInfo()) ?
                roleInfoRepository.findById(accountInfo.getRoleInfoId())
                        .orElseThrow(() -> new AuthServiceException(RestResponseMessage.AUTH_DATA_NOT_FOUND,
                                RestResponseStatusCode.NOT_FOUND_STATUS, RestErrorMessageDetail.ROLE_NOT_FOUND_ERROR_MESSAGE)) : accountInfo.getRoleInfo());
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
    private AccessKeyInfo issueAccessKeyInfoForJwt(Long currentUserId, String mailAddress, String roleName) {
        log.info("START issueAccessKeyInfo.");
        final String token = jwtTokenUtil.generateToken(mailAddress, roleName);
        AccessKeyInfo accesskeyInfo = new AccessKeyInfo();
        accesskeyInfo.setAccessKey(token);
        accesskeyInfo.setAccountInfoId(currentUserId);
        accesskeyInfo.setMailAddress(mailAddress);
        accesskeyInfo.setLastAccessDt(DateUtil.currentTime());
        accesskeyInfo.setCreatedDt(DateUtil.currentTime());
        accesskeyInfo.setUpdatedDt(DateUtil.currentTime());
        accessKeyInfoRepository.save(accesskeyInfo);
        log.info("END issueAccessKeyInfo. accessKeyInfo:{}", accesskeyInfo);

        return accesskeyInfo;
    }

    private PlayerAccessKeyInfo issuePlayerAccessKeyInfoForJwt(Long currentUserId, String mailAddress, String roleName) {
        log.info("START issueAccessKeyInfo.");
        final String token = jwtTokenUtil.generateToken(mailAddress, roleName);
        PlayerAccessKeyInfo playerAccessKeyInfo = new PlayerAccessKeyInfo();
        playerAccessKeyInfo.setAccessKey(token);
        playerAccessKeyInfo.setPlayerInfoId(currentUserId);
        playerAccessKeyInfo.setMailAddress(mailAddress);
        playerAccessKeyInfo.setLastAccessDt(DateUtil.currentTime());
        playerAccessKeyInfo.setCreatedDt(DateUtil.currentTime());
        playerAccessKeyInfo.setUpdatedDt(DateUtil.currentTime());
        playerAccesskeyInfoRepository.save(playerAccessKeyInfo);
        log.info("END issueAccessKeyInfo. accessKeyInfo:{}", playerAccessKeyInfo);

        return playerAccessKeyInfo;
    }

    private AccessKeyInfo issueRefreshAccessKeyInfoForJwt(Long currentUserId, String mailAddress, String roleName, String oldToken) {
        log.info("START issueAccessKeyInfo.");
        final String token = jwtTokenUtil.generateToken(mailAddress, roleName);
        AccessKeyInfo accesskeyInfo = accessKeyInfoRepository.findByAccessKey(oldToken);
        accesskeyInfo.setAccessKey(token);
        accesskeyInfo.setAccountInfoId(currentUserId);
        accesskeyInfo.setLastAccessDt(DateUtil.currentTime());
        accesskeyInfo.setCreatedDt(DateUtil.currentTime());
        accesskeyInfo.setUpdatedDt(DateUtil.currentTime());
        accesskeyInfo.setMailAddress(mailAddress);
        accessKeyInfoRepository.save(accesskeyInfo);

        log.info("END issueAccessKeyInfo. accessKeyInfo:{}", accesskeyInfo);
        return accesskeyInfo;
    }

    @Override
    public RefreshTokenResponse getRefreshToken() throws AuthServiceException {
        AccountInfo accountInfo = accountInfoRepository.findByAccessKey(authCommonService.getAccessKey())
                .orElseThrow(() -> new AuthServiceException(RestResponseMessage.USER_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS, RestErrorMessageDetail.USER_NOT_FOUND_ERROR_MESSAGE));
        setAccountDetails(accountInfo);

        AccessKeyInfo accesskeyInfo = issueRefreshAccessKeyInfoForJwt(accountInfo.getId(), accountInfo.getMailAddress(), accountInfo.getRoleInfo().getRoleName(), authCommonService.getAccessKey());

        return RefreshTokenResponse.builder()
                .userId(accountInfo.getId())
                .roleId(accountInfo.getRoleInfoId())
                .accessKey(accesskeyInfo.getAccessKey())
                .firstName(accountInfo.getFirstName())
                .lastName(accountInfo.getLastName())
                .joiningDt(accountInfo.getJoiningDt())
                .mailAddress(accountInfo.getMailAddress())
                .password(accountInfo.getPassword())
                .deleteFlg(accountInfo.getDeleteFlg())
                .createdUserId(accountInfo.getCreatedUserId())
                .build();
    }

    @Override
    public void logoutWithJwtSecurity() throws AuthServiceException {
        String token = authCommonService.getAccessKey();
        if (!Objects.isNull(accessKeyInfoRepository.findByAccessKey(token))) {
            accessKeyInfoRepository.deleteByAccessKey(token);
        }else if(!Objects.isNull(playerAccesskeyInfoRepository.findByAccessKey(token))){
            playerAccesskeyInfoRepository.deleteByAccessKey(token);
        }else{
            throw new AuthServiceException(RestResponseMessage.NOT_LOGGED_IN, RestResponseStatusCode.NO_ACCESS_STATUS, RestErrorMessageDetail.USER_ALREADY_LOGGED_OUT_ERROR_MESSAGE);
        }
    }
}
