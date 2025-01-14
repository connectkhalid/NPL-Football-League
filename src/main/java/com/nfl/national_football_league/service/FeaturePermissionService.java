package com.nfl.national_football_league.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface FeaturePermissionService {

    @AllArgsConstructor
    class FeaturePermissionMap{
        MainFeature mainFeature;
        SubFeature subFeature;
        Permission permission;
    }

    @Getter
    @Builder
    class FeaturePermissionObject{
        FeaturePermissionMap featurePermissionMap;
        String code;
    }

    enum MainFeature {
        ACCOUNT_MANAGEMENT,
        TEAM_MANAGEMENT,
        CLUB_MANAGEMENT,
        LEAGUE_MANAGEMENT,
        TOURNAMENT_MANAGEMENT,
        SEASON_MANAGEMENT, PROFILE_MANAGEMENT
    }

    enum SubFeature {
        ADMIN_BASIC,
        PLAYER_BASIC,
        TEAM_BASIC,
        CLUB_BASIC,
        LEAGUE_BASIC,
        TOURNAMENT_BASIC,
        ADMIN_PROFILE,
        SEASON_BASIC, PLAYER_PROFILE
    }

    enum Permission {
        VIEW_DETAILS,
        EDIT,
        ADD,
        VIEW_LIST,
        SEARCH,
        DELETE,
        EXPORT
    }
}
