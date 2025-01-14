package com.nfl.national_football_league.service;

import com.nfl.national_football_league.domain.TeamInfo;
import com.nfl.national_football_league.exception.AuthServiceException;
import com.nfl.national_football_league.exception.PlayerServiceException;
import com.nfl.national_football_league.exception.RoleServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

public interface PlayerService {

    @Builder
    @Data
    class PlayerRegisterResponse{
        private long playerId;
        private String firstName;
        private String lastName;
        private String mailAddress;
        private String phoneNumber;
        private String dateOfBirth;
        private String height;
        private String weight;
        private String playingPosition;
        private String nidNumber;
        private String roleName;
        private Timestamp createdDt;
        private Timestamp updatedDt;
        private String imagePath;
    }

    @Builder
    @Data
    class PlayerInfoResponse{
        private long playerId;
        private String firstName;
        private String lastName;
        private String mailAddress;
        private String phoneNumber;
        private String dateOfBirth;
        private String height;
        private String weight;
        private String playingPosition;
        private long teamId;
        private String roleName;
        private String imagePath;
        private String nidNumber;
        private Timestamp createdDt;
        private Timestamp updatedDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PlayerRegisterInputParameter{
        private String firstName;
        private String lastName;
        private String mailAddress;
        private String password;
        private String phoneNumber;
        private String dateOfBirth;
        private String height;
        private String weight;
        private String playingPosition;
        private String nidNumber;
        private long roleCode;
        private String imagePath;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PlayerUpdateInputParameter{
        private long playerId;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String dateOfBirth;
        private String height;
        private String weight;
        private String nidNumber;
        private String playingPosition;
        private String imagePath;
    }

    PlayerService.PlayerRegisterResponse registerPlayer(PlayerService.PlayerRegisterInputParameter parameter) throws PlayerServiceException, RoleServiceException;
    List<PlayerInfoResponse> getPlayerList() throws PlayerServiceException;
    PlayerInfoResponse getPlayerInfo(long playerId) throws PlayerServiceException, AuthServiceException;

    PlayerInfoResponse updatePlayerInfo(PlayerUpdateInputParameter parameter) throws PlayerServiceException, AuthServiceException;
}
