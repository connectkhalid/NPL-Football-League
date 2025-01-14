package com.nfl.national_football_league.constant;


import com.nfl.national_football_league.service.FeaturePermissionService.FeaturePermissionObject;
import com.nfl.national_football_league.service.FeaturePermissionService.MainFeature;
import com.nfl.national_football_league.service.FeaturePermissionService.SubFeature;
import com.nfl.national_football_league.service.FeaturePermissionService.Permission;
import org.springframework.stereotype.Service;
import static com.nfl.national_football_league.service.impl.FeaturePermissionServiceImpl.getFeaturePermissionObject;
@Service("apiPermission")
public class FeaturePermissionList {

    // ACCOUNT MANAGEMENT
    public static FeaturePermissionObject getAdminListViewPermission() {
        return getFeaturePermissionObject(MainFeature.ACCOUNT_MANAGEMENT, SubFeature.ADMIN_BASIC, Permission.VIEW_LIST);
    }

    public static FeaturePermissionObject getPlayerListViewPermission() {
        return getFeaturePermissionObject(MainFeature.ACCOUNT_MANAGEMENT, SubFeature.PLAYER_BASIC, Permission.VIEW_LIST);
    }
    // PROFILE MANAGEMENT
    public static FeaturePermissionObject getAdminProfileViewPermission() {
        return getFeaturePermissionObject(MainFeature.PROFILE_MANAGEMENT, SubFeature.ADMIN_PROFILE, Permission.VIEW_DETAILS);
    }

    public static FeaturePermissionObject getAdminProfileEditPermission() {
        return getFeaturePermissionObject(MainFeature.PROFILE_MANAGEMENT, SubFeature.ADMIN_PROFILE, Permission.EDIT);
    }

    public static FeaturePermissionObject getPlayerProfileViewPermission() {
        return getFeaturePermissionObject(MainFeature.PROFILE_MANAGEMENT, SubFeature.PLAYER_PROFILE, Permission.VIEW_DETAILS);
    }

    public static FeaturePermissionObject getPlayerProfileEditPermission() {
        return getFeaturePermissionObject(MainFeature.PROFILE_MANAGEMENT, SubFeature.PLAYER_PROFILE, Permission.EDIT);
    }

    // TEAM MANAGEMENT
    public static FeaturePermissionObject getTeamListViewPermission() {
        return getFeaturePermissionObject(MainFeature.TEAM_MANAGEMENT, SubFeature.TEAM_BASIC, Permission.VIEW_LIST);
    }

    public static FeaturePermissionObject getTeamCreatePermission() {
        return getFeaturePermissionObject(MainFeature.TEAM_MANAGEMENT, SubFeature.TEAM_BASIC, Permission.ADD);
    }
    public static FeaturePermissionObject getTeamUpdatePermission() {
        return getFeaturePermissionObject(MainFeature.TEAM_MANAGEMENT, SubFeature.TEAM_BASIC, Permission.EDIT);
    }

    // CLUB MANAGEMENT
    public static FeaturePermissionObject getClubListViewPermission() {
        return getFeaturePermissionObject(MainFeature.CLUB_MANAGEMENT, SubFeature.CLUB_BASIC, Permission.VIEW_LIST);
    }

    public static FeaturePermissionObject getClubCreatePermission() {
        return getFeaturePermissionObject(MainFeature.CLUB_MANAGEMENT, SubFeature.CLUB_BASIC, Permission.ADD);
    }

    public static FeaturePermissionObject getClubUpdatePermission(){
        return getFeaturePermissionObject(MainFeature.CLUB_MANAGEMENT, SubFeature.CLUB_BASIC, Permission.EDIT);
    }

    public static FeaturePermissionObject getClubViewDetails() {
        return getFeaturePermissionObject(MainFeature.CLUB_MANAGEMENT, SubFeature.CLUB_BASIC, Permission.VIEW_DETAILS);
    }

    public static FeaturePermissionObject getAddTeamToClubPermission() {
        return getFeaturePermissionObject(MainFeature.CLUB_MANAGEMENT, SubFeature.CLUB_BASIC, Permission.ADD);
    }

    //LEAGUE MANAGEMENT
    public static FeaturePermissionObject getLeagueListViewPermission() {
        return getFeaturePermissionObject(MainFeature.LEAGUE_MANAGEMENT, SubFeature.LEAGUE_BASIC, Permission.VIEW_LIST);
    }

    public static FeaturePermissionObject getLeagueCreatePermission() {
        return getFeaturePermissionObject(MainFeature.LEAGUE_MANAGEMENT, SubFeature.LEAGUE_BASIC, Permission.ADD);
    }
    public static FeaturePermissionObject getLeagueUpdatePermission() {
        return getFeaturePermissionObject(MainFeature.LEAGUE_MANAGEMENT, SubFeature.LEAGUE_BASIC, Permission.EDIT);
    }

    //TOURNAMENT MANAGEMENT

    public static FeaturePermissionObject getTournamentCreatePermission() {
        return getFeaturePermissionObject(MainFeature.TOURNAMENT_MANAGEMENT, SubFeature.TOURNAMENT_BASIC, Permission.ADD);
    }
    public static FeaturePermissionObject getTournamentUpdatePermission() {
        return getFeaturePermissionObject(MainFeature.TOURNAMENT_MANAGEMENT, SubFeature.TOURNAMENT_BASIC, Permission.EDIT);
    }
    public static FeaturePermissionObject getTournamentListViewPermission() {
        return getFeaturePermissionObject(MainFeature.TOURNAMENT_MANAGEMENT, SubFeature.TOURNAMENT_BASIC, Permission.VIEW_LIST);
    }
    public static FeaturePermissionObject getTournamentViewDetails() {
        return getFeaturePermissionObject(MainFeature.TOURNAMENT_MANAGEMENT, SubFeature.TOURNAMENT_BASIC, Permission.VIEW_DETAILS);
    }
    //SEASON MANAGEMENT
    public static FeaturePermissionObject getSeasonCreatePermission() {
        return getFeaturePermissionObject(MainFeature.SEASON_MANAGEMENT, SubFeature.SEASON_BASIC, Permission.ADD);
    }
    public static FeaturePermissionObject getSeasonEditPermission() {
        return getFeaturePermissionObject(MainFeature.SEASON_MANAGEMENT, SubFeature.SEASON_BASIC, Permission.EDIT);
    }
    public static FeaturePermissionObject getSeasonListViewPermission() {
        return getFeaturePermissionObject(MainFeature.SEASON_MANAGEMENT, SubFeature.SEASON_BASIC, Permission.VIEW_LIST);
    }
    public static FeaturePermissionObject getSeasonViewDetails() {
        return getFeaturePermissionObject(MainFeature.SEASON_MANAGEMENT, SubFeature.SEASON_BASIC, Permission.VIEW_DETAILS);
    }
}
