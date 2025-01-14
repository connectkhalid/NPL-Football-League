package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.RestErrorMessageDetail;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.domain.*;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.SeasonServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import com.nfl.national_football_league.exception.TournamentServiceException;
import com.nfl.national_football_league.repository.AccountInfoRepository;
import com.nfl.national_football_league.repository.SeasonInfoRepository;
import com.nfl.national_football_league.repository.TeamInfoRepository;
import com.nfl.national_football_league.repository.TournamentInfoRepository;
import com.nfl.national_football_league.service.AuthCommonService;
import com.nfl.national_football_league.service.TeamService;
import com.nfl.national_football_league.service.TournamentService;
import com.nfl.national_football_league.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.nfl.national_football_league.service.impl.LeagueServiceImpl.getSeasonInfo;
import static com.nfl.national_football_league.service.impl.LeagueServiceImpl.getTeamDataResponse;

@Slf4j
@Service("TournamentService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TournamentServiceImpl implements TournamentService {

    private final TournamentInfoRepository tournamentInfoRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final AuthCommonService authCommonService;
    private final TeamInfoRepository teamInfoRepository;
    private final SeasonInfoRepository seasonInfoRepository;


    @Override
    public TournamentRegisterResponse registerTournament(TournamentRegisterInputParameter parameter) throws  AuthServiceException, TournamentServiceException {
        AccountInfo accountInfo = getCurrentAccountInfo();
        if(tournamentInfoRepository.findByTournamentName(parameter.getTournamentName()).isPresent())
            throw new TournamentServiceException(
                    RestResponseMessage.TOURNAMENT_ALREADY_EXISTS,
                    RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                    RestErrorMessageDetail.TOURNAMENT_ALREADY_EXISTS_ERROR_MESSAGE);
        Timestamp startTime = DateUtil.stringToTimestampWithoutZone(
                parameter.getTournamentStartTime(), "yyyyMMdd");
        Timestamp endTime = DateUtil.stringToTimestampWithoutZone(
                parameter.getTournamentEndTime(), "yyyyMMdd");
        TournamentInfo tournamentInfo = new TournamentInfo();
        tournamentInfo.setTournamentName(parameter.getTournamentName());
        tournamentInfo.setAccountInfoId(accountInfo.getId());
        tournamentInfo.setTournamentOwner(parameter.getTournamentOwner());
        tournamentInfo.setEmail(parameter.getEmail());
        tournamentInfo.setPhoneNumber(parameter.getPhoneNumber());
        tournamentInfo.setAddress(parameter.getAddress());
        tournamentInfo.setImagePath(parameter.getImagePath());
        tournamentInfo.setTournamentYear(parameter.getTournamentYear());
        tournamentInfo.setStartTime(startTime);
        tournamentInfo.setEndTime(endTime);
        tournamentInfo.setDeleteFlg(false);
        tournamentInfo.setCreatedDt(DateUtil.currentTime());
        tournamentInfo.setUpdatedDt(DateUtil.currentTime());
        tournamentInfoRepository.save(tournamentInfo);

        return TournamentRegisterResponse.builder()
                .tournamentId(tournamentInfo.getId())
                .tournamentName(tournamentInfo.getTournamentName())
                .tournamentOwner(tournamentInfo.getTournamentOwner())
                .email(tournamentInfo.getEmail())
                .phoneNumber(tournamentInfo.getPhoneNumber())
                .address(tournamentInfo.getAddress())
                .imagePath(tournamentInfo.getImagePath())
                .tournamentYear(tournamentInfo.getTournamentYear())
                .tournamentStartTime(startTime)
                .tournamentEndTime(endTime)
                .adminId(accountInfo.getId())
                .deleteFlg(false)
                .createdDt(tournamentInfo.getCreatedDt())
                .updatedDt(tournamentInfo.getUpdatedDt())
                .build();
    }

    @Override
    public List<TournamentDataResponse> getTournamentList(){
        return tournamentInfoRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(TournamentInfo::getTournamentName))
                .map(tournamentInfo -> TournamentDataResponse.builder()
                        .tournamentId(tournamentInfo.getId())
                        .tournamentName(tournamentInfo.getTournamentName())
                        .tournamentOwner(tournamentInfo.getTournamentOwner())
                        .adminId(tournamentInfo.getAccountInfoId())
                        .email(tournamentInfo.getEmail())
                        .phoneNumber(tournamentInfo.getPhoneNumber())
                        .address(tournamentInfo.getAddress())
                        .imagePath(tournamentInfo.getImagePath())
                        .tournamentYear(tournamentInfo.getTournamentYear())
                        .tournamentStartTime(tournamentInfo.getStartTime())
                        .tournamentEndTime(tournamentInfo.getEndTime())
                        .deleteFlg(false)
                        .createdDt(tournamentInfo.getCreatedDt())
                        .updatedDt(tournamentInfo.getUpdatedDt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public TeamListTournamentSeasonResponse registerTeamToTournament(TeamTournamentRegisterInputParameter parameter) throws TournamentServiceException, AuthServiceException, SeasonServiceException, TeamServiceException {
        AccountInfo accountInfo = getCurrentAccountInfo();
        TournamentInfo tournamentInfo = retrieveTournamentInfo(parameter.getTournamentInfoId());
        SeasonInfo seasonInfo = retrieveSeasonInfo(parameter.getFkSeasonInfoId(),tournamentInfo);
        TeamInfo targetTeamInfo = teamInfoRepository.findById(parameter.getFkTeamInfoId())
                .orElseThrow(()-> new TeamServiceException(
                        RestResponseMessage.TEAM_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.TEAM_NOT_FOUND_ERROR_MESSAGE));

        List<TeamInfo> teamList = seasonInfo.getTeamList();
        if(teamList.stream().anyMatch(teamInfo -> teamInfo.getId()==parameter.getFkTeamInfoId()))
            throw new SeasonServiceException(
                    RestResponseMessage.TEAM_ALREADY_EXISTS,
                    RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                    RestErrorMessageDetail.TEAM_ALREADY_EXISTS_ERROR_MESSAGE);

        teamList.add(targetTeamInfo);
        seasonInfo.setTeamList(teamList);
        seasonInfo.setAccountInfo(accountInfo);
        seasonInfo.setUpdatedDt(DateUtil.currentTime());

        Set<SeasonInfo> seasonInfos = targetTeamInfo.getAssignedSeasons();
        seasonInfos.add(seasonInfo);
        targetTeamInfo.setAssignedSeasons(seasonInfos);

        teamInfoRepository.save(targetTeamInfo);
        seasonInfoRepository.save(seasonInfo);
        return mapTeamListDataResponse(tournamentInfo, seasonInfo, false);
    }

    @Override
    public TeamListTournamentSeasonResponse getTeamListByTournamentSeason(long tournamentId, long seasonId) throws TournamentServiceException, SeasonServiceException {
        TournamentInfo tournamentInfo = retrieveTournamentInfo(tournamentId);
        SeasonInfo season = retrieveSeasonInfo(seasonId, tournamentInfo);
        return mapTeamListDataResponse(tournamentInfo, season, true );
    }

    // Utility Methods
    private AccountInfo getCurrentAccountInfo() throws AuthServiceException {
        return accountInfoRepository.findByAccessKey(authCommonService.getAccessKey())
                .orElseThrow(() -> new AuthServiceException(
                        RestResponseMessage.USER_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.USER_NOT_FOUND_ERROR_MESSAGE
                ));
    }

    private TeamListTournamentSeasonResponse mapTeamListDataResponse(
            TournamentInfo tournamentInfo, SeasonInfo seasonInfo, boolean skipTeams) {
        List<TeamService.TeamDataResponse> teamResponses = skipTeams
                ? Collections.emptyList()
                : Optional.ofNullable(seasonInfo.getTeamList())
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(TeamInfo::getId))
                .map(this::mapTeamToResponse).toList();
        return TeamListTournamentSeasonResponse.builder()
                .tournamentInfoId(tournamentInfo.getId())
                .fkSeasonInfoId(seasonInfo.getId())
                .teamList(teamResponses)
                .build();
    }

    private TeamService.TeamDataResponse mapTeamToResponse(TeamInfo teamInfo) {
        return getTeamDataResponse(teamInfo);
    }

    private TournamentInfo retrieveTournamentInfo(long tournamentId) throws TournamentServiceException {
        return tournamentInfoRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentServiceException(
                        RestResponseMessage.TOURNAMENT_INFO_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.TOURNAMENT_INFO_NOT_FOUND_ERROR_DETAILS));
    }

    private SeasonInfo retrieveSeasonInfo(long seasonId, TournamentInfo tournamentInfo) throws SeasonServiceException {
        SeasonInfo season = seasonInfoRepository.findById(seasonId)
                .orElseThrow(() -> new SeasonServiceException(
                        RestResponseMessage.SEASON_INFO_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.SEASON_INFO_NOT_FOUND_ERROR_DETAILS));
        return getSeasonInfo(season, tournamentInfo.getSeasonList());
    }
}