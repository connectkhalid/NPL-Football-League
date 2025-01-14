package com.nfl.national_football_league.service;

import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.SeasonServiceException;
import com.nfl.national_football_league.exception.TournamentServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Year;
import java.util.List;

public interface TournamentService {

    @Builder
    @Data
    class TournamentRegisterResponse {
        private long tournamentId;
        private String tournamentName;
        private String tournamentOwner;
        private long adminId;
        private String email;
        private String phoneNumber;
        private String address;
        private String imagePath;
        private Year tournamentYear;
        private Timestamp tournamentStartTime;
        private Timestamp tournamentEndTime;
        private Boolean deleteFlg;
        private Timestamp createdDt;
        private Timestamp updatedDt;
    }

    @Builder
    @Data
    class TournamentDataResponse {
        private long tournamentId;
        private String tournamentName;
        private String tournamentOwner;
        private long adminId;
        private String email;
        private String phoneNumber;
        private String address;
        private String imagePath;
        private Year tournamentYear;
        private Timestamp tournamentStartTime;
        private Timestamp tournamentEndTime;
//        private List<SeasonService.SeasonDataResponse> seasonList;
        private Boolean deleteFlg;
        private Timestamp createdDt;
        private Timestamp updatedDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class TournamentRegisterInputParameter {
        private String tournamentName;
        private String tournamentOwner;
        private String email;
        private String phoneNumber;
        private String address;
        private String imagePath;
        private Year tournamentYear;
        private String tournamentStartTime;
        private String tournamentEndTime;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class TeamListTournamentSeasonResponse {
        private long tournamentInfoId;
        private long fkSeasonInfoId;
        private List<TeamService.TeamDataResponse> teamList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class TeamTournamentRegisterInputParameter {
        private long fkTeamInfoId;
        private long tournamentInfoId;
        private long fkSeasonInfoId;

    }

    TournamentRegisterResponse registerTournament(TournamentRegisterInputParameter parameter) throws AuthServiceException, TournamentServiceException;

    List<TournamentDataResponse> getTournamentList();


    @Transactional(rollbackFor = Exception.class)
    TeamListTournamentSeasonResponse registerTeamToTournament(TeamTournamentRegisterInputParameter parameter) throws TournamentServiceException, AuthServiceException, SeasonServiceException, TeamServiceException;

    @Transactional(rollbackFor = Exception.class)
    TeamListTournamentSeasonResponse getTeamListByTournamentSeason(long tournamentId, long seasonId) throws TournamentServiceException, SeasonServiceException;
}
