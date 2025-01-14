package com.nfl.national_football_league.util;

import com.nfl.national_football_league.domain.AccountInfo;
import com.nfl.national_football_league.domain.PlayerInfo;
import com.nfl.national_football_league.repository.AccountInfoRepository;
import com.nfl.national_football_league.repository.PlayerInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParameterCheckInDatabaseUtil {

    private final AccountInfoRepository accountInfoRepository;
    private final PlayerInfoRepository playerInfoRepository;

    public boolean checkMailinDatabase(String mail) {
        AccountInfo accountInfo = accountInfoRepository.findByMailAddressAndDeleteFlgIsFalse(mail);
        if(!Objects.isNull(accountInfo)){
            log.error("Mail Address already in use AccountInfo.");
            return false;
        }

        PlayerInfo playerInfo = playerInfoRepository.findByMailAddressAndDeleteFlgIsFalse(mail);
        if(!Objects.isNull(playerInfo)){
            log.error("Mail Address already in use PlayerInfo.");
            return false;
        }

        return true;
    }
}
