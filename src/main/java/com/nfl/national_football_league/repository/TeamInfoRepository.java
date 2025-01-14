package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.constant.TeamCategory;
import com.nfl.national_football_league.domain.RoleInfo;
import com.nfl.national_football_league.domain.TeamInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository("TeamInfoRepository")
@Transactional(rollbackFor = Exception.class)
public interface TeamInfoRepository extends JpaRepository<TeamInfo, Long> {

//    @Query(value =
//            " SELECT ti.* "
//                    + " FROM team_info ti "
//                    + "WHERE LOWER(ti.delete_flg) = FALSE  "
//                    + "ORDER BY ti.created_dt DESC "
//            , nativeQuery = true)
//    List<TeamInfo> findByTeamType();

    Optional<TeamInfo> findByTeamName(String teamName);

    TeamInfo findByIdAndAndDeleteFlgIsFalse(long id);

    List<TeamInfo> findByTeamCategory(TeamCategory teamCategory);
}
