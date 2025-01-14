package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.LeagueInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository("LeagueInfoRepository")
@Transactional(rollbackFor = Exception.class)
public interface LeagueInfoRepository extends JpaRepository<LeagueInfo, Long> {

    @Query(value =
            " SELECT li.* "
                    + " FROM league_info li "
                    + "WHERE LOWER(li.delete_flg) = FALSE  "
                    + "ORDER BY li.created_dt DESC "
            , nativeQuery = true)
    List<LeagueInfo> getAllLeagueInfo();

    Optional<LeagueInfo> findByLeagueName(String leagueName);
}
