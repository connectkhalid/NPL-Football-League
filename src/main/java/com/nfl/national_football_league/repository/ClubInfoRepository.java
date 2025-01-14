package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.ClubInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository("ClubInfoRepository")
@Transactional(rollbackFor = Exception.class)
public interface ClubInfoRepository extends JpaRepository<ClubInfo, Long> {

    @Query(value =
            " SELECT ci.* "
                    + " FROM club_info ci "
                    + "WHERE LOWER(ci.delete_flg) = FALSE "
                    + "ORDER BY ci.created_dt ASC "
            , nativeQuery = true)
    List<ClubInfo> getAllClubs();

    ClubInfo findByIdAndAndDeleteFlgIsFalse(long id);

    Optional<ClubInfo> findByClubName(String clubName);
}
