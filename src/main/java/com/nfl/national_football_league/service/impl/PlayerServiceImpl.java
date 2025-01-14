package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.RestErrorMessageDetail;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.domain.PlayerInfo;
import com.nfl.national_football_league.domain.RoleInfo;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.PlayerServiceException;
import com.nfl.national_football_league.exception.RoleServiceException;
import com.nfl.national_football_league.repository.PlayerInfoRepository;
import com.nfl.national_football_league.repository.RoleInfoRepository;
import com.nfl.national_football_league.service.AuthCommonService;
import com.nfl.national_football_league.service.PlayerService;
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
@Service("PlayerService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerServiceImpl implements PlayerService {

    private final PlayerInfoRepository playerInfoRepository;
    private final RoleInfoRepository roleInfoRepository;
    private final AuthCommonService authCommonService;

    @Override
    public PlayerRegisterResponse registerPlayer(PlayerRegisterInputParameter parameter) throws PlayerServiceException, RoleServiceException {
        RoleInfo roleInfo = roleInfoRepository.findByRoleCode(parameter.getRoleCode());
        if(Objects.isNull(roleInfo)){
            log.info("role nothing.");
            throw new RoleServiceException(RestResponseMessage.ROLE_NOT_FOUND,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.BAD_CREDENTIALS_ERROR_MESSAGE);
        }
        if(playerInfoRepository.findByMailAddress(parameter.getMailAddress()).isPresent())
                throw new PlayerServiceException(
                        RestResponseMessage.INFO_PLAYER_ALREADY_EXISTS,
                        RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                        RestErrorMessageDetail.PLAYER_ALREADY_EXISTS_ERROR_DETAILS
                );
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setFirstName(parameter.getFirstName());
        playerInfo.setLastName(parameter.getLastName());
        playerInfo.setMailAddress(parameter.getMailAddress());
        playerInfo.setPassword(StringUtil.encodeBCrypt(parameter.getPassword()));
        playerInfo.setPhoneNumber(parameter.getPhoneNumber());
        playerInfo.setDateOfBirth(DateUtil.stringToTimestampWithoutZone(parameter.getDateOfBirth(), "yyyyMMdd"));
        playerInfo.setHeight(parameter.getHeight());
        playerInfo.setWeight(parameter.getWeight());
        playerInfo.setPlayingPosition(parameter.getPlayingPosition());
        playerInfo.setNidNumber(parameter.getNidNumber());
//        playerInfo.setImagePath(parameter.getImagePath());
        playerInfo.setDeleteFlg(false);
        playerInfo.setRoleInfoId(roleInfo.getId());
        playerInfo.setCreatedDt(DateUtil.currentTime());
        playerInfo.setUpdatedDt(DateUtil.currentTime());
        playerInfo.setJoiningDt(DateUtil.currentTime());
        playerInfoRepository.save(playerInfo);

        return PlayerRegisterResponse.builder()
                .playerId(playerInfo.getId())
                .firstName(playerInfo.getFirstName())
                .lastName(playerInfo.getLastName())
                .mailAddress(playerInfo.getMailAddress())
                .phoneNumber(playerInfo.getPhoneNumber())
                .dateOfBirth(playerInfo.getDateOfBirth().toString())
                .height(playerInfo.getHeight())
                .weight(playerInfo.getWeight())
                .playingPosition(playerInfo.getPlayingPosition())
                .nidNumber(playerInfo.getNidNumber())
                .roleName(roleInfo.getRoleName())
                .createdDt(DateUtil.currentTime())
                .updatedDt(DateUtil.currentTime())
//                .imagePath(playerInfo.getImagePath())
                .build();
    }

    @Override
    public List<PlayerInfoResponse> getPlayerList(){
        return playerInfoRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(PlayerInfo::getId))
                .map(playerInfo -> PlayerInfoResponse.builder()
                        .playerId(playerInfo.getId())
                        .firstName(playerInfo.getFirstName())
                        .lastName(playerInfo.getLastName())
                        .mailAddress(playerInfo.getMailAddress())
                        .phoneNumber(playerInfo.getPhoneNumber())
                        .dateOfBirth(DateUtil.timestampToString(playerInfo.getDateOfBirth(), "yyyyMMdd"))
                        .height(playerInfo.getHeight())
                        .weight(playerInfo.getWeight())
                        .playingPosition(playerInfo.getPlayingPosition())
                        .teamId(Objects.isNull(playerInfo.getTeamInfoId()) ? 0 : playerInfo.getTeamInfoId()) //showing nullPointerException
                        .nidNumber(playerInfo.getNidNumber())
                        .roleName(playerInfo.getRoleInfo().getRoleName())
                        .imagePath(playerInfo.getImagePath())
                        .createdDt(playerInfo.getCreatedDt())
                        .updatedDt(playerInfo.getUpdatedDt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public PlayerInfoResponse getPlayerInfo(long playerId) throws PlayerServiceException, AuthServiceException {

        Optional<PlayerInfo> currentUserPlayerInfo = playerInfoRepository.findByAccessKey(authCommonService.getAccessKey());
        if(currentUserPlayerInfo.isPresent() && currentUserPlayerInfo.get().getId() != playerId){
            log.info("No Access Permission.");
            throw new AuthServiceException(RestResponseMessage.ACCESS_DENIED,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.NO_PERMISSION_ERROR_MESSAGE);
        }

        Optional<PlayerInfo> playerInfo = playerInfoRepository.findById(playerId);
        if(playerInfo.isEmpty()){
            throw new PlayerServiceException(RestResponseMessage.PLAYER_NOT_FOUND,
                    RestResponseStatusCode.NOT_FOUND_STATUS, RestErrorMessageDetail.PLAYER_NOT_FOUND_ERROR_MESSAGE);
        }

        return getPlayerInfoResponse(playerInfo.get());
    }

    @Override
    public PlayerInfoResponse updatePlayerInfo(PlayerUpdateInputParameter parameter) throws PlayerServiceException, AuthServiceException {
        Optional<PlayerInfo> currentUserPlayerInfo = playerInfoRepository.findByAccessKey(authCommonService.getAccessKey());
        if(currentUserPlayerInfo.isPresent() && currentUserPlayerInfo.get().getId() != parameter.getPlayerId()){
            log.info("No Access Permission.");
            throw new AuthServiceException(RestResponseMessage.ACCESS_DENIED,
                    RestResponseStatusCode.VALIDATION_ERROR_STATUS, RestErrorMessageDetail.NO_PERMISSION_ERROR_MESSAGE);
        }

        Optional<PlayerInfo> playerInfo = playerInfoRepository.findById(parameter.getPlayerId());
        if(playerInfo.isEmpty()){
            throw new PlayerServiceException(RestResponseMessage.PLAYER_NOT_FOUND,
                    RestResponseStatusCode.NOT_FOUND_STATUS, RestErrorMessageDetail.PLAYER_NOT_FOUND_ERROR_MESSAGE);
        }

        playerInfo.get().setFirstName(parameter.getFirstName());
        playerInfo.get().setLastName(parameter.getLastName());
        playerInfo.get().setPhoneNumber(parameter.getPhoneNumber());
        playerInfo.get().setDateOfBirth(DateUtil.stringToTimestampWithoutZone(parameter.getDateOfBirth(), "yyyyMMdd"));
        playerInfo.get().setHeight(parameter.getHeight());
        playerInfo.get().setWeight(parameter.getWeight());
        playerInfo.get().setPlayingPosition(parameter.getPlayingPosition());
        playerInfo.get().setNidNumber(parameter.getNidNumber());
        playerInfo.get().setImagePath(parameter.getImagePath());
        playerInfoRepository.save(playerInfo.get());

        return getPlayerInfoResponse(playerInfo.get());

    }

    private PlayerInfoResponse getPlayerInfoResponse(PlayerInfo playerInfo){
        return PlayerInfoResponse.builder()
                .playerId(playerInfo.getId())
                .firstName(playerInfo.getFirstName())
                .lastName(playerInfo.getLastName())
                .mailAddress(playerInfo.getMailAddress())
                .phoneNumber(playerInfo.getPhoneNumber())
                .dateOfBirth(DateUtil.timestampToString(playerInfo.getDateOfBirth(),"yyyyMMdd"))
                .height(playerInfo.getHeight())
                .weight(playerInfo.getWeight())
                .playingPosition(playerInfo.getPlayingPosition())
                .teamId(playerInfo.getTeamInfoId())
                .roleName(playerInfo.getRoleInfo().getRoleName())
                .nidNumber(playerInfo.getNidNumber())
                .imagePath(playerInfo.getImagePath())
                .createdDt(playerInfo.getCreatedDt())
                .updatedDt(playerInfo.getUpdatedDt())
                .build();
    }
}
