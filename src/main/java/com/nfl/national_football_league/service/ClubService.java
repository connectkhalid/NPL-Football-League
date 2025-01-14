package com.nfl.national_football_league.service;

import com.nfl.national_football_league.domain.TeamInfo;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.ClubServiceException;
import com.nfl.national_football_league.exception.TeamServiceException;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

public interface ClubService {

//    @Builder
//    @Data
//    class ClubRegisterResponse{
//        private Long clubId;
//        private String clubName;
//        private String clubOwner;
//        private String email;
//        private String phoneNumber;
//        private String address;
//        private String tire;
//        private String imagePath;
//        private Boolean deleteFlg;
//        private long adminId;
//        private Timestamp createdDt;
//        private Timestamp updatedDt;
//    }

    @Builder
    @Data
    class ClubDataResponse{
        private long clubId;
        private String clubName;
        private long adminId;
        private String clubOwner;
        private String email;
        private String phoneNumber;
        private String address;
        private String tire;
        private String imagePath;
        private Boolean deleteFlg;
        private Timestamp createdDt;
        private Timestamp updatedDt;
        private List<TeamService.TeamDataResponse> teams;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ClubInfoInputParameter{
        private long clubId;
        private String clubName;
        private String clubOwner;
        private String email;
        private String phoneNumber;
        private String address;
        private String tire;
        private String imagePath;

        public ClubInfoInputParameter(String clubName, String clubOwner, String email, String phoneNumber, String address, String tire) {
            this.clubName = clubName;
            this.clubOwner = clubOwner;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.tire = tire;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class TeamRegisterToClubInputParameter{
        private long clubId;
        private long fkTeamId;
    }

    ClubDataResponse registerClub(ClubInfoInputParameter parameter) throws TeamServiceException, AuthServiceException, ClubServiceException;

    ClubDataResponse updateClub(ClubInfoInputParameter parameter) throws TeamServiceException, AuthServiceException, ClubServiceException;

    List<ClubDataResponse> getClubList() throws TeamServiceException;

    ClubDataResponse registerTeamToClub(TeamRegisterToClubInputParameter parameter) throws ClubServiceException, TeamServiceException;
}
