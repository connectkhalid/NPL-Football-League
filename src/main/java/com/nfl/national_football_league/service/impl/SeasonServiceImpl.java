package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.constant.RestErrorMessageDetail;
import com.nfl.national_football_league.constant.RestResponseMessage;
import com.nfl.national_football_league.constant.RestResponseStatusCode;
import com.nfl.national_football_league.domain.*;
import com.nfl.national_football_league.constant.SeasonCategory;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.ClubServiceException;
import com.nfl.national_football_league.exception.SeasonServiceException;
import com.nfl.national_football_league.exception.TournamentServiceException;
import com.nfl.national_football_league.repository.*;
import com.nfl.national_football_league.service.AuthCommonService;
import com.nfl.national_football_league.service.SeasonService;
import com.nfl.national_football_league.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service("SeasonService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SeasonServiceImpl implements SeasonService {

    private final AccountInfoRepository accountInfoRepository;
    private final AuthCommonService authCommonService;
    private final SeasonInfoRepository seasonInfoRepository;
    private final LeagueInfoRepository leagueInfoRepository;
    private final TournamentInfoRepository tournamentInfoRepository;

    @Override
    public SeasonDataResponse registerSeason(SeasonRegisterInputParameter parameter)
            throws SeasonServiceException, AuthServiceException, ClubServiceException, TournamentServiceException {
        AccountInfo accountInfo = accountInfoRepository.findByAccessKey(authCommonService.getAccessKey())
                .orElseThrow(() -> new AuthServiceException(RestResponseMessage.USER_NOT_FOUND,
                        RestResponseStatusCode.NOT_FOUND_STATUS, RestErrorMessageDetail.USER_NOT_FOUND_ERROR_MESSAGE));
        Timestamp startTime = DateUtil.stringToTimestampWithoutZone(
                parameter.getStartDate(), "yyyyMMdd");
        Timestamp endTime = DateUtil.stringToTimestampWithoutZone(
                parameter.getEndDate(), "yyyyMMdd");

        if(seasonInfoRepository.findBySeasonCategoryAndCategoryNameAndSeasonYearAndStartDateAndEndDate( parameter.getSessionCategory(),
                parameter.getCategoryName(), parameter.getSeasonYear(), startTime, endTime).isPresent())
            throw new SeasonServiceException(
                    RestResponseMessage.SESSION_INFO_ALREADY_EXISTS,
                    RestResponseStatusCode.ALREADY_EXISTS_STATUS,
                    RestErrorMessageDetail.SEASON_ALREADY_EXISTS_ERROR_MESSAGE);

        SeasonInfo seasonInfo = new SeasonInfo();


        seasonInfo.setSeasonYear(parameter.getSeasonYear());
        seasonInfo.setAccountInfoId(accountInfo.getId());
        seasonInfo.setStartDate(startTime);
        seasonInfo.setEndDate(endTime);
        seasonInfo.setLocation(parameter.getLocation());
        seasonInfo.setDeleteFlg(false);
        seasonInfo.setCreatedDt(DateUtil.currentTime());
        seasonInfo.setUpdatedDt(DateUtil.currentTime());

        if(Objects.equals(parameter.getSessionCategory(), SeasonCategory.LEAGUE)) {
            LeagueInfo leagueInfo = leagueInfoRepository.findByLeagueName(parameter.getCategoryName())
                    .orElseThrow(()->new ClubServiceException(
                            RestResponseMessage.LEAGUE_NOT_FOUND,
                            RestResponseStatusCode.NOT_FOUND_STATUS,
                            RestErrorMessageDetail.LEAGUE_NOT_FOUND_ERROR_MESSAGE));
            seasonInfo.setCategoryId(leagueInfo.getId());
            seasonInfo.setCategoryName(leagueInfo.getLeagueName());
            seasonInfo.setSeasonCategory(SeasonCategory.LEAGUE);

//            Set<SeasonInfo> seasonInfos = leagueInfo.getSeasonList();
//            seasonInfos.add(seasonInfo);
//            leagueInfo.setSeasonList(seasonInfos);
//            leagueInfoRepository.save(leagueInfo);
        }

        if(Objects.equals(parameter.getSessionCategory(), SeasonCategory.TOURNAMENT)) {
            TournamentInfo tournamentInfo = tournamentInfoRepository.findByTournamentName(parameter.getCategoryName())
                    .orElseThrow(()-> new TournamentServiceException(RestResponseMessage.TOURNAMENT_INFO_NOT_FOUND,
                            RestResponseStatusCode.NOT_FOUND_STATUS,
                            RestErrorMessageDetail.TOURNAMENT_INFO_NOT_FOUND_ERROR_DETAILS));

            seasonInfo.setCategoryId(tournamentInfo.getId());
            seasonInfo.setCategoryName(tournamentInfo.getTournamentName());
            seasonInfo.setSeasonCategory(SeasonCategory.TOURNAMENT);

//            Set<SeasonInfo> seasonInfos = tournamentInfo.getSeasonList();
//            seasonInfos.add(seasonInfo);
//            tournamentInfo.setSeasonList(seasonInfos);
//            tournamentInfoRepository.save(tournamentInfo);
        }
        seasonInfoRepository.save(seasonInfo);

        return SeasonDataResponse.builder()
                .seasonId(seasonInfo.getId())
                .categoryId(seasonInfo.getCategoryId())
                .categoryName(seasonInfo.getCategoryName())
                .seasonCategory(seasonInfo.getSeasonCategory().toString())
                .seasonYear(seasonInfo.getSeasonYear())
                .startDate(seasonInfo.getStartDate())
                .endDate(seasonInfo.getEndDate())
                .location(seasonInfo.getLocation())
                .createdDt(seasonInfo.getCreatedDt())
                .deleteFlg(seasonInfo.getDeleteFlg())
                .build();
    }
}
