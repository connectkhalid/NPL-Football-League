package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.RestErrorMessageDetail;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.domain.AccountInfo;
import com.nfl.national_football_league.domain.RoleInfo;
import com.nfl.national_football_league.exception.AccountServiceException;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.RoleServiceException;
import com.nfl.national_football_league.repository.AccountInfoRepository;
import com.nfl.national_football_league.repository.RoleInfoRepository;
import com.nfl.national_football_league.service.AccountService;
import com.nfl.national_football_league.service.AuthCommonService;
import com.nfl.national_football_league.util.DateUtil;
import com.nfl.national_football_league.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("AccountService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountServiceImpl implements AccountService {

     private final AccountInfoRepository accountInfoRepository;
     private final RoleInfoRepository roleInfoRepository;
     private final AuthCommonService authCommonService;

    @Override
    public AccountRegisterResponse registerAccount(AccountRegisterInputParameter parameter) throws AccountServiceException, RoleServiceException {

        RoleInfo roleInfo = roleInfoRepository.findByRoleCode(parameter.getRoleCode());
        if(Objects.isNull(roleInfo)){
            log.info("role nothing.");
            throw new RoleServiceException(RestResponseMessage.ROLE_NOT_FOUND,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.BAD_CREDENTIALS_ERROR_MESSAGE);
        }

        long currentUserId = 0;
        Optional<AccountInfo> currentUserAccountInfo = accountInfoRepository.findByAccessKey(authCommonService.getAccessKey());
        if(currentUserAccountInfo.isPresent()){
            currentUserId = currentUserAccountInfo.get().getId();
        }

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setRoleInfoId(roleInfo.getId());
        accountInfo.setFirstName(parameter.getFirstName());
        accountInfo.setLastName(parameter.getLastName());
        accountInfo.setJoiningDt(DateUtil.currentTime());
        accountInfo.setMailAddress(parameter.getMailAddress());
        accountInfo.setPassword(StringUtil.encodeBCrypt(parameter.getPassword()));
        accountInfo.setPhoneNumber(parameter.getPhoneNumber());
        accountInfo.setLocation(parameter.getLocation());
        accountInfo.setDeleteFlg(false);
        accountInfo.setCreatedUserId(currentUserId);
        accountInfo.setCreatedDt(DateUtil.currentTime());
        accountInfo.setUpdatedDt(DateUtil.currentTime());
        accountInfo.setLastLoginDt(DateUtil.currentTime());

        accountInfoRepository.save(accountInfo);
        return AccountRegisterResponse.builder()
                .firstName(parameter.getFirstName())
                .lastName(parameter.getLastName())
                .mailAddress(parameter.getMailAddress())
                .password(parameter.getPassword())
                .phoneNumber(parameter.getPhoneNumber())
                .location(parameter.getLocation())
                .roleName(roleInfo.getRoleName())
                .build();
    }

    @Override
    public AccountInfo getAccountInformationByEmail(String mailAddress) throws AccountServiceException, AuthServiceException {
        AccountInfo accountInfo = accountInfoRepository.findByMailAddressAndDeleteFlgIsFalse(mailAddress);
        if(Objects.isNull(accountInfo)){
            log.info("account nothing.");
            throw new AuthServiceException(RestResponseMessage.WRONG_CREDENTIALS,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.BAD_CREDENTIALS_ERROR_MESSAGE);
        }
        return accountInfo;
    }

    @Override
    public List<AccountRegisterResponse> getAdminList() throws AccountServiceException {

        return accountInfoRepository.findAll()
                .stream()
                .filter(accountInfo -> accountInfo.getId() != 1)
                .sorted(Comparator.comparing(AccountInfo::getId))
                .map(accountInfo -> AccountRegisterResponse.builder()
                        .adminId(accountInfo.getId())
                        .firstName(accountInfo.getFirstName())
                        .lastName(accountInfo.getLastName())
                        .mailAddress(accountInfo.getMailAddress())
                        .password(accountInfo.getPassword())
                        .phoneNumber(accountInfo.getPhoneNumber())
                        .location(accountInfo.getLocation())
                        .roleName(accountInfo.getRoleInfo().getRoleName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public AccountRegisterResponse getAdminInfo(long adminId) throws AccountServiceException, AuthServiceException {

        Optional<AccountInfo> currentUserAccountInfo = accountInfoRepository.findByAccessKey(authCommonService.getAccessKey());
        if(currentUserAccountInfo.isEmpty() || (currentUserAccountInfo.get().getRoleInfoId() != 1 && currentUserAccountInfo.get().getId() != adminId)){
            log.info("account nothing.");
            throw new AuthServiceException(RestResponseMessage.ACCESS_DENIED,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.NO_PERMISSION_ERROR_MESSAGE);
        }

        Optional<AccountInfo> accountInfo = accountInfoRepository.findById(adminId);
        if(accountInfo.isEmpty()){
            log.info("account nothing.");
            throw new AuthServiceException(RestResponseMessage.WRONG_CREDENTIALS,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.BAD_CREDENTIALS_ERROR_MESSAGE);
        }

        return AccountRegisterResponse.builder()
                .adminId(accountInfo.get().getId())
                .firstName(accountInfo.get().getFirstName())
                .lastName(accountInfo.get().getLastName())
                .mailAddress(accountInfo.get().getMailAddress())
                .password(accountInfo.get().getPassword())
                .phoneNumber(accountInfo.get().getPhoneNumber())
                .location(accountInfo.get().getLocation())
                .roleName(accountInfo.get().getRoleInfo().getRoleName())
                .build();
    }

    @Override
    public AccountRegisterResponse updateAdminInfo(AccountUpdateInputParameter parameter) throws AccountServiceException, AuthServiceException {
        Optional<AccountInfo> currentUserAccountInfo = accountInfoRepository.findByAccessKey(authCommonService.getAccessKey());
        if(currentUserAccountInfo.isEmpty() || (currentUserAccountInfo.get().getRoleInfoId() != 1 && currentUserAccountInfo.get().getId() != parameter.getAdminId())){
            log.info("account nothing.");
            throw new AuthServiceException(RestResponseMessage.ACCESS_DENIED,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.NO_PERMISSION_ERROR_MESSAGE);
        }

        Optional<AccountInfo> accountInfo = accountInfoRepository.findById(parameter.getAdminId());
        if(accountInfo.isEmpty()){
            log.info("account nothing.");
            throw new AuthServiceException(RestResponseMessage.WRONG_CREDENTIALS,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.BAD_CREDENTIALS_ERROR_MESSAGE);
        }

        accountInfo.get().setFirstName(parameter.getFirstName());
        accountInfo.get().setLastName(parameter.getLastName());
        accountInfo.get().setPhoneNumber(parameter.getPhoneNumber());
        accountInfo.get().setLocation(parameter.getLocation());

        accountInfoRepository.save(accountInfo.get());

        return AccountRegisterResponse.builder()
                .adminId(accountInfo.get().getId())
                .firstName(accountInfo.get().getFirstName())
                .lastName(accountInfo.get().getLastName())
                .mailAddress(accountInfo.get().getMailAddress())
                .password(accountInfo.get().getPassword())
                .phoneNumber(accountInfo.get().getPhoneNumber())
                .location(accountInfo.get().getLocation())
                .roleName(accountInfo.get().getRoleInfo().getRoleName())
                .build();
    }
}
