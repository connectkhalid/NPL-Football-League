package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.AccountInfo;
import com.nfl.national_football_league.domain.PlayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerInfoRepository extends JpaRepository<PlayerInfo, Long> {
    PlayerInfo findByMailAddressAndDeleteFlgIsFalse(String mailAddress);


    @Query(value =
            "SELECT pi.* "
                    + "FROM player_info pi "
                    + "INNER JOIN player_access_key_info pakey "
                    + "ON pi.id = pakey.FK_player_info_id "
                    + "WHERE pakey.access_key = :accessKey and pi.delete_flg is false "
            , nativeQuery = true)
    Optional<PlayerInfo> findByAccessKey(@Param("accessKey") String accessKey);

    List<PlayerInfo> findAllByTeamInfoIdAndDeleteFlgFalse(long teamId);

    Optional<PlayerInfo> findByMailAddress(String mailAddress);
}
