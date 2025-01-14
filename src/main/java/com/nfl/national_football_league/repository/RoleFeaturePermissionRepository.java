package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.RoleFeaturePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface RoleFeaturePermissionRepository extends JpaRepository<RoleFeaturePermission, Long> {
    @Query(value = "SELECT CONCAT(sfi.sub_feature_code, '_', pi.permission_code) " +
            "FROM role_feature_permission " +
            "         JOIN role_info ri on ri.id = role_feature_permission.FK_role_info_id " +
            "         JOIN feature_permission fp on fp.id = role_feature_permission.FK_feature_permission_id " +
            "         JOIN permission_info pi on pi.id = fp.FK_permission_info_id " +
            "         JOIN sub_feature_info sfi on sfi.id = fp.FK_sub_feature_info_id " +
            "         JOIN main_feature_info mfi on mfi.id = sfi.FK_main_feature_info_id " +
            "WHERE ri.role_code = ?1 " +
            "  AND ri.delete_flg = false ", nativeQuery = true)
    List<String> findAllFeaturePermissionByRoleCode(Long roleCode);
}
