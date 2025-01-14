package com.nfl.national_football_league.security;

import com.nfl.national_football_league.domain.AccountInfo;
import com.nfl.national_football_league.domain.PlayerInfo;
import com.nfl.national_football_league.domain.RoleInfo;
import com.nfl.national_football_league.repository.AccountInfoRepository;
import com.nfl.national_football_league.repository.PlayerInfoRepository;
import com.nfl.national_football_league.repository.RoleFeaturePermissionRepository;
import com.nfl.national_football_league.repository.RoleInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtInMemoryUserDetailsService implements UserDetailsService {

    private final AccountInfoRepository accountInfoRepository;
    private final PlayerInfoRepository playerInfoRepository;
    //private final RoleInfoRepository roleInfoRepository;
    private final RoleFeaturePermissionRepository roleFeaturePermissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountInfo accountInfo = accountInfoRepository.findByMailAddressAndDeleteFlgIsFalse(username);
        PlayerInfo playerInfo = playerInfoRepository.findByMailAddressAndDeleteFlgIsFalse(username);

        if(Objects.isNull(accountInfo) && Objects.isNull(playerInfo))
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));

        if(!Objects.isNull(playerInfo)){
            List<SimpleGrantedAuthority> featurePermissionList = roleFeaturePermissionRepository
                    .findAllFeaturePermissionByRoleCode(playerInfo.getRoleInfo().getRoleCode())
                    .stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new JwtUserDetails(playerInfo.getId(), playerInfo.getMailAddress(),
                    playerInfo.getPassword(), featurePermissionList);
        }

        //List<SimpleGrantedAuthority> RoleInfoList = roleInfoRepository.findAll()
                //.stream().map(RoleInfo::getRoleName).collect(Collectors.toList())
               // .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        List<SimpleGrantedAuthority> featurePermissionList = roleFeaturePermissionRepository
                .findAllFeaturePermissionByRoleCode(accountInfo.getRoleInfo().getRoleCode())
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new JwtUserDetails(accountInfo.getId(), accountInfo.getMailAddress(),
                accountInfo.getPassword(), featurePermissionList);
    }
}
