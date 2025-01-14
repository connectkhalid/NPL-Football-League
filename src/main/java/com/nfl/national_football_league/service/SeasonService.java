package com.nfl.national_football_league.service;

import com.nfl.national_football_league.constant.SeasonCategory;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.ClubServiceException;
import com.nfl.national_football_league.exception.SeasonServiceException;
import com.nfl.national_football_league.exception.TournamentServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Year;

public interface SeasonService {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SeasonRegisterInputParameter{
        private SeasonCategory sessionCategory;
        private String categoryName;
        private String location;
        private Year seasonYear;
        private String startDate;
        private String endDate;
    }
    @Builder
    @Data
    class SeasonDataResponse{
        private long seasonId;
        private long categoryId;
        private String categoryName;
        private String seasonCategory;
        private String location;
        private Year seasonYear;
        private Timestamp startDate;
        private Timestamp endDate;
        private Timestamp createdDt;
        private Boolean deleteFlg;

    }

    SeasonDataResponse registerSeason(SeasonRegisterInputParameter parameter) throws SeasonServiceException, AuthServiceException, ClubServiceException, TournamentServiceException;
}
