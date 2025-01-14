package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.RestErrorMessageDetail;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.domain.*;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.LeagueServiceException;
import com.nfl.national_football_league.exception.SeasonServiceException;
import com.nfl.national_football_league.repository.AccountInfoRepository;
import com.nfl.national_football_league.repository.LeagueInfoRepository;
import com.nfl.national_football_league.repository.SeasonInfoRepository;
import com.nfl.national_football_league.repository.TeamInfoRepository;
import com.nfl.national_football_league.service.AuthCommonService;
import com.nfl.national_football_league.service.LeagueService;
import com.nfl.national_football_league.service.TeamService;
import com.nfl.national_football_league.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service("LeagueService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LeagueServiceImpl implements LeagueService {

    private final LeagueInfoRepository leagueInfoRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final AuthCommonService authCommonService;
    private final TeamInfoRepository teamInfoRepository;
    private final SeasonInfoRepository seasonInfoRepository;

    @Override
    public LeagueRegisterResponse registerLeague(LeagueInputParameter parameter) throws AuthServiceException, LeagueServiceException {
        AccountInfo accountInfo = getCurrentAccountInfo();
        if(leagueInfoRepository.findByLeagueName(parameter.getLeagueName()).isPresent())
            throw new LeagueServiceException(RestResponseMessage.LEAGUE_ALREADY_EXISTS,
                    RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                    RestErrorMessageDetail.LEAGUE_ALREADY_EXISTS_ERROR_MESSAGE);

        LeagueInfo leagueInfo = new LeagueInfo();
        return getLeagueResponse(parameter, accountInfo, leagueInfo);
    }

    @Override
    public LeagueRegisterResponse updateLeague(LeagueInputParameter parameter) throws AuthServiceException, LeagueServiceException {
        AccountInfo accountInfo = getCurrentAccountInfo();
        LeagueInfo targetLeagueInfo = leagueInfoRepository.findById(parameter.getLeagueId())
                .orElseThrow(()-> new LeagueServiceException(RestResponseMessage.LEAGUE_NOT_FOUND,
                    RestResponseStatusCode.NOT_FOUND_STATUS,
                    RestErrorMessageDetail.LEAGUE_NOT_FOUND_ERROR_MESSAGE));

        return getLeagueResponse(parameter, accountInfo, targetLeagueInfo);
    }

    @Override
    public List<LeagueDataResponse> getLeagueList() {
        return leagueInfoRepository.getAllLeagueInfo()
                .stream()
                .sorted(Comparator.comparing(LeagueInfo::getId))
                .map(leagueInfo -> mapLeagueToResponse(leagueInfo, true))
                .collect(Collectors.toList());
    }

    @Override
    public LeagueDataResponse registerTeamToLeague(TeamLeagueRegisterInputParameter parameter) throws LeagueServiceException, AuthServiceException, SeasonServiceException {
        TeamInfo teamInfo = getTeamInfoById(parameter.getFkTeamInfoId());
        LeagueInfo leagueInfo = getLeagueInfoById(parameter.getLeagueInfoId());
        SeasonInfo seasonInfo = getSeasonInfoById(parameter.getFkSeasonInfoId(),leagueInfo);
        AccountInfo currentAccountInfo = getCurrentAccountInfo();

        List<TeamInfo> teamList = seasonInfo.getTeamList();
        // Check if the team is already in the league
        if (teamList.stream().anyMatch(team -> team.getId() == teamInfo.getId())) {
            throw new LeagueServiceException(
                    RestResponseMessage.TEAM_LEAGUE_INFO_ALREADY_EXISTS,
                    RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                    RestErrorMessageDetail.TEAM_LEAGUE_ALREADY_EXISTS_ERROR_MESSAGE
            );
        }

        // Update team and league entity
        teamList.add(teamInfo);
        seasonInfo.setTeamList(teamList);
        seasonInfo.setAccountInfo(currentAccountInfo);
        seasonInfo.setUpdatedDt(DateUtil.currentTime());

        Set<SeasonInfo> seasonInfos = teamInfo.getAssignedSeasons();
        seasonInfos.add(seasonInfo);
        teamInfo.setAssignedSeasons(seasonInfos);

        teamInfoRepository.save(teamInfo);
        seasonInfoRepository.save(seasonInfo);

        return mapLeagueToResponse(leagueInfo, false);
    }

    @Override
    public LeagueDataResponse getTeamListLeague(long leagueId, long seasonId) throws LeagueServiceException, SeasonServiceException {
        LeagueInfo leagueInfo = getLeagueInfoById(leagueId);
        getSeasonInfoById(seasonId, leagueInfo);
        return mapLeagueToResponse(leagueInfo, false);
    }

    // Utility Methods
    private LeagueRegisterResponse getLeagueResponse(
            LeagueInputParameter parameter, AccountInfo accountInfo, LeagueInfo targetLeagueInfo) {
        targetLeagueInfo.setLeagueName(parameter.getLeagueName());
        targetLeagueInfo.setAccountInfo(accountInfo);
        targetLeagueInfo.setAccountInfoId(accountInfo.getId());
        targetLeagueInfo.setLeagueOwner(parameter.getLeagueOwner());
        targetLeagueInfo.setEmail(parameter.getEmail());
        targetLeagueInfo.setPhoneNumber(parameter.getPhoneNumber());
        targetLeagueInfo.setAddress(parameter.getAddress());
        targetLeagueInfo.setTire(parameter.getTire());
        if(parameter.getImagePath()!=null)
            targetLeagueInfo.setImagePath(parameter.getImagePath());
        targetLeagueInfo.setDeleteFlg(false);
        targetLeagueInfo.setCreatedDt(DateUtil.currentTime());
        targetLeagueInfo.setUpdatedDt(DateUtil.currentTime());
        leagueInfoRepository.save(targetLeagueInfo);

        return LeagueRegisterResponse.builder()
                .leagueId(targetLeagueInfo.getId())
                .leagueName(targetLeagueInfo.getLeagueName())
                .leagueOwner(targetLeagueInfo.getLeagueOwner())
                .email(targetLeagueInfo.getEmail())
                .phoneNumber(targetLeagueInfo.getPhoneNumber())
                .address(targetLeagueInfo.getAddress())
                .tire(targetLeagueInfo.getTire())
                .adminId(accountInfo.getId())
                .imagePath(targetLeagueInfo.getImagePath())
                .deleteFlg(false)
                .createdDt(targetLeagueInfo.getCreatedDt())
                .updatedDt(targetLeagueInfo.getUpdatedDt())
                .build();
    }

    private AccountInfo getCurrentAccountInfo() throws AuthServiceException {
        return accountInfoRepository.findByAccessKey(authCommonService.getAccessKey())
                .orElseThrow(() -> new AuthServiceException(
                        RestResponseMessage.USER_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.USER_NOT_FOUND_ERROR_MESSAGE
                ));
    }

    private LeagueInfo getLeagueInfoById(Long leagueId) throws LeagueServiceException {
        return leagueInfoRepository.findById(leagueId)
                .orElseThrow(() -> new LeagueServiceException(
                        RestResponseMessage.LEAGUE_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.LEAGUE_NOT_FOUND_ERROR_MESSAGE
                ));
    }

    private TeamInfo getTeamInfoById(Long teamId) throws LeagueServiceException {
        return teamInfoRepository.findById(teamId)
                .orElseThrow(() -> new LeagueServiceException(
                        RestResponseMessage.TEAM_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.PLAYER_NOT_FOUND_ERROR_MESSAGE
                ));
    }

    private SeasonInfo getSeasonInfoById(long seasonId, LeagueInfo leagueInfo) throws LeagueServiceException, SeasonServiceException {
        SeasonInfo season = seasonInfoRepository.findById(seasonId)
                .orElseThrow(() -> new LeagueServiceException(
                        RestResponseMessage.SEASON_INFO_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.SEASON_INFO_NOT_FOUND_ERROR_DETAILS));
        return getSeasonInfo(season, leagueInfo.getSeasonList());
    }

    static SeasonInfo getSeasonInfo(SeasonInfo season, Set<SeasonInfo> seasonList) throws SeasonServiceException {
        boolean seasonExists = seasonList.stream().anyMatch(seasonInfo -> seasonInfo.getId() == season.getId());
        if (!seasonExists) {
            throw new SeasonServiceException(
                    RestResponseMessage.SEASON_INFO_NOT_FOUND,
                    RestResponseStatusCode.NOT_FOUND_STATUS,
                    RestErrorMessageDetail.SEASON_INFO_NOT_FOUND_ERROR_DETAILS
            );
        }
        return season;
    }

    private LeagueDataResponse mapLeagueToResponse(LeagueInfo leagueInfo, boolean skipTeams) {
        List<TeamService.TeamDataResponse> teamResponses = retrieveTeamsForLeague(leagueInfo, skipTeams);

        return LeagueDataResponse.builder()
                .id(leagueInfo.getId())
                .leagueName(leagueInfo.getLeagueName())
                .leagueOwner(leagueInfo.getLeagueOwner())
                .adminId(leagueInfo.getAccountInfoId())
                .email(leagueInfo.getEmail())
                .phoneNumber(leagueInfo.getPhoneNumber())
                .address(leagueInfo.getAddress())
                .tire(leagueInfo.getTire())
                .imagePath(leagueInfo.getImagePath())
                .teamList(teamResponses)
                .deleteFlg(leagueInfo.getDeleteFlg())
                .createdDt(leagueInfo.getCreatedDt())
                .updatedDt(leagueInfo.getUpdatedDt())
                .build();
    }

    static TeamService.TeamDataResponse getTeamDataResponse(TeamInfo teamInfo) {
        return TeamService.TeamDataResponse.builder()
                .teamId(teamInfo.getId())
                .teamName(teamInfo.getTeamName())
                .teamOwner(teamInfo.getTeamOwner())
                .teamCategory(teamInfo.getTeamCategory().name())
                .email(teamInfo.getEmail())
                .phoneNumber(teamInfo.getPhoneNumber())
                .address(teamInfo.getAddress())
                .tire(teamInfo.getTires())
                .imagePath(teamInfo.getImagePath())
                .deleteFlg(teamInfo.getDeleteFlg())
                .adminId(teamInfo.getAccountInfoId())
                .createdDt(teamInfo.getCreatedDt())
                .updatedDt(teamInfo.getUpdatedDt())
                .build();
    }

    private List<TeamService.TeamDataResponse> retrieveTeamsForLeague(LeagueInfo leagueInfo, boolean skipTeams) {
        if (skipTeams) return Collections.emptyList();

        return leagueInfo.getSeasonList().stream()
                .flatMap(season -> season.getTeamList().stream())
                .distinct()
                .map(this::mapTeamToResponse)
                .toList();
    }
    private TeamService.TeamDataResponse mapTeamToResponse(TeamInfo teamInfo) {
        return getTeamDataResponse(teamInfo);
    }
}

