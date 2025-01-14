package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository("AccountInfoRepository")
@Transactional(rollbackFor = Exception.class)
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {

    AccountInfo findByMailAddressAndDeleteFlgIsFalse(String mailAddress);

    @Query(value =
            "SELECT ai.* "
                    + "FROM account_info ai "
                    + "WHERE ai.mail_address = :mailAddress "
            , nativeQuery = true)
    AccountInfo findByMailAddress(@Param("mailAddress") String mailAddress);

    @Query(value =
            "SELECT ai.* "
                    + "FROM account_info ai "
                    + "INNER JOIN access_key_info akey "
                    + "ON ai.id = akey.FK_account_info_id "
                    + "WHERE akey.access_key = :accessKey and ai.delete_flg is false "
            , nativeQuery = true)
    Optional<AccountInfo> findByAccessKey(@Param("accessKey") String accessKey);

    @Query(value = "select ai from AccountInfo as ai join" +
            " ai.accessKeyInfos ak join ai.roleInfo ri" +
            " where ak.accessKey=?1 and ai.deleteFlg is false "
    )
    Optional<AccountInfo> findByAccessKeyWithRoleInfo(@Param("accessKey") String accessKey);
}
