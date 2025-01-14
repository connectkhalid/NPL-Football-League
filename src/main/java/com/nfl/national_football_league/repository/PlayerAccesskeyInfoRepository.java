package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.PlayerAccessKeyInfo;
import com.nfl.national_football_league.domain.PlayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository("PlayerAccesskeyInfoRepository")
@Transactional(rollbackFor = Exception.class)
public interface PlayerAccesskeyInfoRepository extends JpaRepository<PlayerAccessKeyInfo, Long> {

    PlayerAccessKeyInfo findByAccessKey(String accessKey);

    @Modifying
    @Query(value = "DELETE FROM player_access_key_info WHERE access_key = :accessKey", nativeQuery = true)
    void deleteByAccessKey(String accessKey);

    @Modifying
    @Query(value = "DELETE FROM player_access_key_info WHERE player_access_key_info.mail_address = ?1 AND player_access_key_info.created_dt < ?2", nativeQuery = true)
    void deleteByMailAddressAndExpDtLessThan(String mailAddress, Timestamp time);
}
