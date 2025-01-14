package com.nfl.national_football_league.service;

import com.nfl.national_football_league.domain.PlayerInfo;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.PlayerServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

public interface TeamService {

    @Builder
    @Data
    class TeamRegisterResponse{
        private String teamName;
        private String teamCategory;
        private long adminId;
        private String teamOwner;
        private String email;
        private String phoneNumber;
        private String address;
        private int tire;
        private String imagePath;
        private List<PlayerInfo> playerList;
        private Boolean deleteFlg;
        private Timestamp createdDt;
        private Timestamp updatedDt;
    }

    @Builder
    @Data
    class TeamDataResponse{
        private long teamId;
        private long adminId;
        private String teamName;
        private String teamCategory;
        private String teamOwner;
        private String email;
        private String phoneNumber;
        private String address;
        private int tire;
        private String imagePath;
        private List<PlayerService.PlayerInfoResponse> playerList;
        private Boolean deleteFlg;
        private Timestamp createdDt;
        private Timestamp updatedDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class TeamInfoInputParameter {
        private long teamId;
        private String teamName;
        private String teamCategory;
        private String teamOwner;
        private String email;
        private String phoneNumber;
        private String address;
        private int tire;
        private String imagePath;

        public TeamInfoInputParameter(String teamName, String teamCategory, String teamOwner, String email, String phoneNumber, String address, int tire) {
            this.teamName = teamName;
            this.teamCategory = teamCategory;
            this.teamOwner = teamOwner;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.tire = tire;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PlayerTeamRegisterInputParameter{
        private long fkPlayerInfoId;
        private long fkTeamInfoId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class InputTeamCategory{
        private String teamCategory;
    }

    TeamDataResponse registerTeam(TeamInfoInputParameter parameter) throws TeamServiceException, AuthServiceException;

    TeamDataResponse updateTeam(TeamInfoInputParameter parameter) throws TeamServiceException, AuthServiceException;

    List<TeamDataResponse> getTeamList() throws TeamServiceException;

    List<TeamDataResponse> getTeamListByCategory(InputTeamCategory inputTeamCategory) throws TeamServiceException;

    TeamDataResponse registerPlayerByTeam(PlayerTeamRegisterInputParameter parameter) throws TeamServiceException, PlayerServiceException;

    List<PlayerService.PlayerInfoResponse> getTeamPlayerListByTeamId(long teamId) throws TeamServiceException;

}
