package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.RestErrorMessageDetail;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.constant.TeamCategory;
import com.nfl.national_football_league.domain.AccountInfo;
import com.nfl.national_football_league.domain.PlayerInfo;
import com.nfl.national_football_league.domain.TeamInfo;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.PlayerServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import com.nfl.national_football_league.form.validation.EnumUtility;
import com.nfl.national_football_league.repository.AccountInfoRepository;
import com.nfl.national_football_league.repository.PlayerInfoRepository;
import com.nfl.national_football_league.repository.TeamInfoRepository;
import com.nfl.national_football_league.service.AuthCommonService;
import com.nfl.national_football_league.service.PlayerService;
import com.nfl.national_football_league.service.TeamService;
import com.nfl.national_football_league.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("TeamService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamServiceImpl implements TeamService {

    private final TeamInfoRepository teamInfoRepository;
    private final PlayerInfoRepository playerInfoRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final AuthCommonService authCommonService;

    @Override
    public TeamDataResponse registerTeam(TeamInfoInputParameter parameter) throws TeamServiceException, AuthServiceException {
        AccountInfo accountInfo = accountInfoRepository.findByAccessKey(authCommonService.getAccessKey())
                .orElseThrow(() -> new AuthServiceException(RestResponseMessage.USER_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS, RestErrorMessageDetail.USER_NOT_FOUND_ERROR_MESSAGE));

        if(teamInfoRepository.findByTeamName(parameter.getTeamName()).isPresent())
                throw new TeamServiceException(
                        RestResponseMessage.TEAM_ALREADY_EXISTS,
                        RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                        RestErrorMessageDetail.TEAM_ALREADY_EXISTS_ERROR_MESSAGE);

        TeamInfo teamInfo = new TeamInfo();
        return mapTeamInfoToTeamDataResponse(buildTeamInfo(parameter, accountInfo, teamInfo), true);
    }

    @Override
    public TeamDataResponse updateTeam(TeamInfoInputParameter parameter) throws TeamServiceException, AuthServiceException {
        AccountInfo accountInfo = accountInfoRepository.findByAccessKey(authCommonService.getAccessKey())
                .orElseThrow(() -> new AuthServiceException(RestResponseMessage.USER_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS, RestErrorMessageDetail.USER_NOT_FOUND_ERROR_MESSAGE));

        TeamInfo teamInfo = teamInfoRepository.findById(parameter.getTeamId())
                .orElseThrow(()-> new TeamServiceException(
                    RestResponseMessage.TEAM_NOT_FOUND,
                    RestResponseStatusCode.NOT_FOUND_STATUS,
                    RestErrorMessageDetail.TEAM_NOT_FOUND_ERROR_MESSAGE));

        return mapTeamInfoToTeamDataResponse(buildTeamInfo(parameter, accountInfo, teamInfo), false);
    }

    private TeamInfo buildTeamInfo(TeamInfoInputParameter parameter, AccountInfo accountInfo, TeamInfo teamInfo) {
        TeamCategory teamCategory  = EnumUtility.getEnumValue(TeamCategory.class, parameter.getTeamCategory());
        teamInfo.setTeamName(parameter.getTeamName());
        teamInfo.setTeamCategory(teamCategory);
        teamInfo.setAccountInfoId(accountInfo.getId());
        teamInfo.setAccountInfo(accountInfo);
        teamInfo.setTeamOwner(parameter.getTeamOwner());
        teamInfo.setEmail(parameter.getEmail());
        teamInfo.setPhoneNumber(parameter.getPhoneNumber());
        teamInfo.setAddress(parameter.getAddress());
        teamInfo.setTires(parameter.getTire());
        if(parameter.getImagePath()!=null)
            teamInfo.setImagePath(parameter.getImagePath());
        teamInfo.setDeleteFlg(false);
        teamInfo.setCreatedDt(DateUtil.currentTime());
        teamInfo.setUpdatedDt(DateUtil.currentTime());
        return teamInfoRepository.save(teamInfo);
    }

    @Override
    public List<TeamDataResponse> getTeamList() throws TeamServiceException {
        List<TeamInfo> teams = teamInfoRepository.findAll();
        if(teams.isEmpty())
            throw new TeamServiceException(
                    RestResponseMessage.TEAM_NOT_FOUND,
                    RestResponseStatusCode.NOT_FOUND_STATUS,
                    RestErrorMessageDetail.TEAM_NOT_FOUND_ERROR_MESSAGE);

        return  teams.stream()
                .sorted(Comparator.comparing(TeamInfo::getId))
                .map(teamInfo -> mapTeamInfoToTeamDataResponse(teamInfo, true))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamDataResponse> getTeamListByCategory(InputTeamCategory parameter) throws TeamServiceException {
        TeamCategory teamCategory = EnumUtility.getEnumValue(TeamCategory.class, parameter.getTeamCategory());
        List<TeamInfo> teamInfoListByCategory = teamInfoRepository.findByTeamCategory(teamCategory);
        if (teamInfoListByCategory.isEmpty()) {
            throw new TeamServiceException(
                    RestResponseMessage.TEAM_NOT_FOUND,
                    RestResponseStatusCode.NOT_FOUND_STATUS,
                    RestErrorMessageDetail.TEAM_NOT_FOUND_ERROR_MESSAGE);
        }

        return teamInfoListByCategory.stream()
                .sorted(Comparator.comparing(TeamInfo::getId))
                .map(teamInfo -> mapTeamInfoToTeamDataResponse(teamInfo, true))  // Pass true to exclude players
                .collect(Collectors.toList());
    }

    @Override
    public TeamDataResponse registerPlayerByTeam(PlayerTeamRegisterInputParameter parameter) throws TeamServiceException, PlayerServiceException {
        TeamInfo teamInfo = teamInfoRepository.findById(parameter.getFkTeamInfoId())
                .orElseThrow(() -> new TeamServiceException(
                        RestResponseMessage.TEAM_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.TEAM_NOT_FOUND_ERROR_MESSAGE));

        PlayerInfo playerInfo = playerInfoRepository.findById(parameter.getFkPlayerInfoId())
                .orElseThrow(() -> new PlayerServiceException(
                        RestResponseMessage.PLAYER_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.PLAYER_NOT_FOUND_ERROR_MESSAGE));
        if(playerInfo.getTeamInfoId()!=null)
            throw new PlayerServiceException(
                    RestResponseMessage.PLAYER_TEAM_INFO_EXIST,
                    RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                    RestErrorMessageDetail.PLAYER_TEAM_ALREADY_EXISTS_ERROR_DETAILS
            );
        //This check is optional
        List<PlayerInfo> assignedPlayer = teamInfo.getPlayers();
        if (assignedPlayer.stream().anyMatch(player -> Objects.equals(player.getId(), playerInfo.getId()))) {
            throw new TeamServiceException(
                    RestResponseMessage.PLAYER_TEAM_INFO_ALREADY_EXIST,
                    RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                    RestErrorMessageDetail.PLAYER_ALREADY_EXISTS_IN_TEAM_ERROR_DETAILS
            );
        }
        //save player to TeamInfo
        assignedPlayer.add(playerInfo);
        teamInfo.setPlayers(assignedPlayer);
        teamInfoRepository.save(teamInfo);

        //save teamId to playerEntity
        playerInfo.setTeamInfoId(parameter.getFkTeamInfoId());
        playerInfoRepository.save(playerInfo);


        return mapTeamInfoToTeamDataResponse(teamInfo, false);
    }

    @Override
    public List<PlayerService.PlayerInfoResponse> getTeamPlayerListByTeamId(long teamId) throws TeamServiceException {
        TeamInfo teamInfo = teamInfoRepository.findById(teamId)
                .orElseThrow(() -> new TeamServiceException(
                        RestResponseMessage.TEAM_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.TEAM_NOT_FOUND_ERROR_MESSAGE));

        List<PlayerInfo> assignedPlayer = teamInfo.getPlayers();
        if(assignedPlayer==null)
            throw new TeamServiceException(
                    RestResponseMessage.NO_PLAYER_ASSIGNED_IN_TEAM,
                    RestResponseStatusCode.NOT_FOUND_STATUS,
                    RestErrorMessageDetail.NO_PLAYER_ASSIGNED_IN_TEAM_ERROR_MESSAGE);

        return mapTeamInfoToTeamDataResponse(teamInfo, false).getPlayerList();
    }

    //Utility Classes
    private TeamDataResponse mapTeamInfoToTeamDataResponse(TeamInfo teamInfo, boolean skipPlayers) {
        List<PlayerService.PlayerInfoResponse> playerInfoResponse = skipPlayers
                ? Collections.emptyList()
                : Optional.ofNullable(teamInfo.getPlayers())
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(PlayerInfo::getId))
                .map(playerInfo -> PlayerService.PlayerInfoResponse.builder()
                        .playerId(playerInfo.getId())
                        .firstName(playerInfo.getFirstName())
                        .lastName(playerInfo.getLastName())
                        .mailAddress(playerInfo.getMailAddress())
                        .phoneNumber(playerInfo.getPhoneNumber())
                        .dateOfBirth(DateUtil.timestampToString(playerInfo.getDateOfBirth(), "yyyyMMdd"))
                        .height(playerInfo.getHeight())
                        .weight(playerInfo.getWeight())
                        .playingPosition(playerInfo.getPlayingPosition())
                        .nidNumber(playerInfo.getNidNumber())
                        .teamId(playerInfo.getTeamInfoId())
                        .roleName(playerInfo.getRoleInfo().getRoleName())
                        .imagePath(playerInfo.getImagePath())
                        .createdDt(playerInfo.getCreatedDt())
                        .updatedDt(playerInfo.getUpdatedDt())
                        .build())
                .collect(Collectors.toList());

        return TeamDataResponse.builder()
                .teamId(teamInfo.getId())
                .teamName(teamInfo.getTeamName())
                .teamCategory(teamInfo.getTeamCategory().toString())
                .teamOwner(teamInfo.getTeamOwner())
                .email(teamInfo.getEmail())
                .phoneNumber(teamInfo.getPhoneNumber())
                .address(teamInfo.getAddress())
                .tire(teamInfo.getTires())
                .imagePath(teamInfo.getImagePath())
                .playerList(playerInfoResponse)  // Can be empty if excludePlayers is true
                .deleteFlg(teamInfo.getDeleteFlg())
                .adminId(teamInfo.getAccountInfoId())
                .createdDt(teamInfo.getCreatedDt())
                .updatedDt(teamInfo.getUpdatedDt())
                .build();
    }
}