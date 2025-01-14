package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.RestErrorMessageDetail;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.domain.AccountInfo;
import com.nfl.national_football_league.domain.ClubInfo;
import com.nfl.national_football_league.domain.TeamInfo;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.ClubServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import com.nfl.national_football_league.repository.AccountInfoRepository;
import com.nfl.national_football_league.repository.ClubInfoRepository;
import com.nfl.national_football_league.repository.TeamInfoRepository;
import com.nfl.national_football_league.service.AuthCommonService;
import com.nfl.national_football_league.service.ClubService;
import com.nfl.national_football_league.service.TeamService;
import com.nfl.national_football_league.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("ClubService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClubServiceImpl implements ClubService {
    private final TeamInfoRepository teamInfoRepository;

    private final ClubInfoRepository clubInfoRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final AuthCommonService authCommonService;

    @Override
    public ClubDataResponse registerClub(ClubInfoInputParameter parameter) throws AuthServiceException, ClubServiceException {
        AccountInfo accountInfo = accountInfoRepository.findByAccessKey(authCommonService.getAccessKey())
                .orElseThrow(() -> new AuthServiceException(RestResponseMessage.USER_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS, RestErrorMessageDetail.USER_NOT_FOUND_ERROR_MESSAGE));

        if(clubInfoRepository.findByClubName(parameter.getClubName()).isPresent())
            throw new ClubServiceException(RestResponseMessage.CLUB_ALREADY_EXISTS,
                    RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                    RestErrorMessageDetail.CLUB_ALREADY_EXISTS_ERROR_MESSAGE);

        ClubInfo clubInfo = new ClubInfo();
        clubInfo.setClubName(parameter.getClubName());
        clubInfo.setAccountInfo(accountInfo);
        clubInfo.setAccountInfoId(accountInfo.getId());
        clubInfo.setClubOwner(parameter.getClubOwner());
        clubInfo.setEmail(parameter.getEmail());
        clubInfo.setPhoneNumber(parameter.getPhoneNumber());
        clubInfo.setAddress(parameter.getAddress());
        clubInfo.setTire(parameter.getTire());
        clubInfo.setDeleteFlg(false);
        clubInfo.setCreatedDt(DateUtil.currentTime());
        clubInfo.setUpdatedDt(DateUtil.currentTime());
        clubInfoRepository.save(clubInfo);
        return buildClubDataResponse(clubInfo);
    }

    @Override
    public ClubDataResponse updateClub(ClubInfoInputParameter parameter) throws AuthServiceException, ClubServiceException {
        AccountInfo accountInfo = accountInfoRepository.findByAccessKey(authCommonService.getAccessKey())
                .orElseThrow(() -> new AuthServiceException(RestResponseMessage.USER_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS, RestErrorMessageDetail.USER_NOT_FOUND_ERROR_MESSAGE));
        ClubInfo targetClubInfo = clubInfoRepository.findById(parameter.getClubId())
                .orElseThrow( ()-> new ClubServiceException(
                        RestResponseMessage.CLUB_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS,
                        RestErrorMessageDetail.CLUB_NOT_FOUND_ERROR_MESSAGE));

        targetClubInfo.setClubName(parameter.getClubName());
        targetClubInfo.setAccountInfoId(accountInfo.getId());
        targetClubInfo.setClubOwner(parameter.getClubOwner());
        targetClubInfo.setEmail(parameter.getEmail());
        targetClubInfo.setPhoneNumber(parameter.getPhoneNumber());
        targetClubInfo.setAddress(parameter.getAddress());
        targetClubInfo.setTire(parameter.getTire());
        targetClubInfo.setImagePath(parameter.getImagePath());
        targetClubInfo.setDeleteFlg(false);
        targetClubInfo.setCreatedDt(DateUtil.currentTime());
        targetClubInfo.setUpdatedDt(DateUtil.currentTime());
        return buildClubDataResponse(clubInfoRepository.save(targetClubInfo));
    }

    @Override
    public List<ClubDataResponse> getClubList() {
        return clubInfoRepository.getAllClubs()
                .stream()
                .sorted(Comparator.comparing(ClubInfo::getId)) // Sort by Club ID
                .map(this::buildClubDataResponse) // Use helper method to map ClubDataResponse
                .collect(Collectors.toList());
    }

    @Override
    public ClubDataResponse registerTeamToClub(TeamRegisterToClubInputParameter parameter) throws ClubServiceException, TeamServiceException {
        ClubInfo clubInfo = clubInfoRepository.findByIdAndAndDeleteFlgIsFalse(parameter.getClubId());
        if (clubInfo == null)
            throw new ClubServiceException(RestResponseMessage.CLUB_NOT_FOUND,
                    RestResponseStatusCode.NOT_FOUND_STATUS,
                    RestErrorMessageDetail.CLUB_NOT_FOUND_ERROR_MESSAGE);

        TeamInfo teamInfo = teamInfoRepository.findByIdAndAndDeleteFlgIsFalse(parameter.getFkTeamId());
        if (teamInfo == null)
            throw new TeamServiceException(RestResponseMessage.TEAM_NOT_FOUND,
                    RestResponseStatusCode.NOT_FOUND_STATUS,
                    RestErrorMessageDetail.TEAM_NOT_FOUND_ERROR_MESSAGE);

        List<TeamInfo> assignedTeam = clubInfo.getTeams();
        if (assignedTeam.stream().anyMatch(team -> team.getId() == teamInfo.getId())) {
            throw new TeamServiceException(
                    RestResponseMessage.TEAM_ALREADY_EXISTS_IN_CLUB,
                    RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                    RestErrorMessageDetail.TEAM_CLUB_ALREADY_EXISTS_ERROR_MESSAGE
            );
        }
        assignedTeam.add(teamInfo);
        clubInfo.setTeams(assignedTeam);
        clubInfoRepository.save(clubInfo);

        teamInfo.setClubInfo(clubInfo);
        teamInfoRepository.save(teamInfo);
        return buildClubDataResponse(clubInfo);
    }

    //Utility Functions
    private TeamService.TeamDataResponse buildTeamDataResponse(TeamInfo teamInfo) {
        return TeamService.TeamDataResponse.builder()
                .teamId(teamInfo.getId())
                .teamName(teamInfo.getTeamName())
                .teamOwner(teamInfo.getTeamOwner())
                .email(teamInfo.getEmail())
                .phoneNumber(teamInfo.getPhoneNumber())
                .address(teamInfo.getAddress())
                .tire(teamInfo.getTires())
                .imagePath(teamInfo.getImagePath())
                .deleteFlg(teamInfo.getDeleteFlg())
                .createdDt(teamInfo.getCreatedDt())
                .updatedDt(teamInfo.getUpdatedDt())
                .build();
    }
    private ClubDataResponse buildClubDataResponse(ClubInfo clubInfo) {
        List<TeamService.TeamDataResponse> teamDataResponses = null;

        if (clubInfo.getTeams() != null) {
            teamDataResponses = clubInfo.getTeams().stream()
                    .map(this::buildTeamDataResponse) // Map each team to TeamDataResponse
                    .collect(Collectors.toList());
        }
        return ClubDataResponse.builder()
                .clubId(clubInfo.getId())
                .clubName(clubInfo.getClubName())
                .adminId(clubInfo.getAccountInfo().getId())
                .clubOwner(clubInfo.getClubOwner())
                .email(clubInfo.getEmail())
                .phoneNumber(clubInfo.getPhoneNumber())
                .address(clubInfo.getAddress())
                .tire(clubInfo.getTire())
                .imagePath(clubInfo.getImagePath())
                .deleteFlg(clubInfo.getDeleteFlg())
                .createdDt(clubInfo.getCreatedDt())
                .updatedDt(clubInfo.getUpdatedDt())
                .teams(teamDataResponses) // Include the list of teams
                .build();
    }
}
