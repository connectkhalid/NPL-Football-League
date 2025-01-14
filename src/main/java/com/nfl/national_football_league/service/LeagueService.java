package com.nfl.national_football_league.service;

import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.LeagueServiceException;
import com.nfl.national_football_league.exception.SeasonServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface LeagueService {

    @Builder
    @Data
    class LeagueRegisterResponse{
        private long leagueId;
        private String leagueName;
        private String leagueOwner;
        private long adminId;
        private String email;
        private String phoneNumber;
        private String address;
        private int tire;
        private String imagePath;
        private Boolean deleteFlg;
        private Timestamp createdDt;
        private Timestamp updatedDt;
    }

    @Builder
    @Data
    class LeagueDataResponse{
        private long id;
        private String leagueName;
        private String leagueOwner;
        private long adminId;
        private String email;
        private String phoneNumber;
        private String address;
        private int tire;
        private String imagePath;
        private List<TeamService.TeamDataResponse> teamList;
        private Boolean deleteFlg;
        private Timestamp createdDt;
        private Timestamp updatedDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class LeagueInputParameter {
        private long leagueId;
        private String leagueName;
        private String leagueOwner;
        private String email;
        private String phoneNumber;
        private String address;
        private int tire;
        private String imagePath;

        public LeagueInputParameter(String leagueName, String leagueOwner, String email, String phoneNumber, String address, int tire) {
            this.leagueName = leagueName;
            this.leagueOwner= leagueOwner;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.tire = tire;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class TeamLeagueResponse{
        private long fkTeamInfoId;
        private long fkLeagueInfoId;
        private Boolean deleteFlag;
        private Timestamp createdAt;
        private Timestamp updatedDt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class TeamListLeagueResponse{
        private long fkLeagueInfoId;
        private List<TeamService.TeamDataResponse> teamList;
        private Boolean deleteFlag;
        private Timestamp createdAt;
        private Timestamp updatedDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class TeamLeagueRegisterInputParameter {
        private long fkTeamInfoId;
        private long fkSeasonInfoId;
        private long LeagueInfoId;
    }

    LeagueRegisterResponse registerLeague(LeagueInputParameter parameter) throws TeamServiceException, AuthServiceException, LeagueServiceException;

    LeagueRegisterResponse updateLeague(LeagueInputParameter parameter) throws  AuthServiceException, LeagueServiceException;

    List<LeagueDataResponse> getLeagueList() throws TeamServiceException;


    @Transactional(rollbackFor = Exception.class)
    LeagueDataResponse registerTeamToLeague(TeamLeagueRegisterInputParameter parameter) throws LeagueServiceException, AuthServiceException, SeasonServiceException;

    @Transactional(rollbackFor = Exception.class)
    LeagueDataResponse getTeamListLeague(long leagueId, long seasonId) throws TeamServiceException, LeagueServiceException, SeasonServiceException;
}
