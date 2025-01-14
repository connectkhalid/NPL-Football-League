package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository("RoleInfoRepository")
@Transactional(rollbackFor = Exception.class)
public interface RoleInfoRepository extends JpaRepository<RoleInfo, Long> {

    RoleInfo findByRoleCode(Long roleCode);

    @Query(value =
            " SELECT ri.* "
                    + " FROM role_info ri "
                    + " INNER JOIN account_info ai "
                    + "   ON ri.id = ai.FK_role_info_id "
                    + " INNER JOIN access_key_info akey "
                    + "   ON ai.id = akey.FK_account_info_id "
                    + "WHERE akey.access_key = :accessKey and ai.delete_flg is false "
            , nativeQuery = true)
    Optional<RoleInfo> findByAccessKey(@Param("accessKey") String accessKey);

    @Query(value =
            " SELECT ri.* "
                    + " FROM role_info ri "
                    + " INNER JOIN player_info pi "
                    + "   ON ri.id = pi.FK_role_info_id "
                    + " INNER JOIN player_access_key_info pakey "
                    + "   ON pi.id = pakey.FK_player_info_id "
                    + "WHERE pakey.access_key = :accessKey and pi.delete_flg is false "
            , nativeQuery = true)
    Optional<RoleInfo> findByPlayerAccessKey(@Param("accessKey") String accessKey);

    @Query(value =
            " SELECT ri.* "
                    + " FROM role_info ri "
                    + " INNER JOIN account_info ai "
                    + "   ON ri.id = ai.FK_role_info_id "
                    + "WHERE ai.mail_address = :mailAddress and ai.delete_flg is false "
            , nativeQuery = true)
    Optional<RoleInfo> findByMailAddress(@Param("mailAddress") String mailAddress);

}
