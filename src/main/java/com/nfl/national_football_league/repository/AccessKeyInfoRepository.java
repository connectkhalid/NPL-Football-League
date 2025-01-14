package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.AccessKeyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository("AccessKeyInfoRepository")
@Transactional(rollbackFor = Exception.class)
public interface AccessKeyInfoRepository extends JpaRepository<AccessKeyInfo, Long> {

    AccessKeyInfo findByAccessKey(String accessKey);

    @Modifying
    @Query(value = "DELETE FROM access_key_info WHERE access_key = :accessKey", nativeQuery = true)
    void deleteByAccessKey(String accessKey);

    @Modifying
    @Query(value = "DELETE FROM access_key_info WHERE access_key_info.mail_address = ?1 AND access_key_info.created_dt < ?2", nativeQuery = true)
    void deleteByMailAddressAndExpDtLessThan(String mailAddress, Timestamp time);

    @Modifying
    @Query(value = "DELETE FROM AccessKeyInfo ai WHERE ai.mailAddress = ?1 AND ai.accessKey <> ?2")
    void deleteByMailAddressAndCurrentToken(String mailAddress, String currentAccessKey);
}
