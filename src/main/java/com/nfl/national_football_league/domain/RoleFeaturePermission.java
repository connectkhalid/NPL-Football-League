package com.nfl.national_football_league.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "role_feature_permission")
@Getter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class RoleFeaturePermission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "FK_role_info_id", nullable = false)
    private long roleInfoId;

    @Column(name = "FK_feature_permission_id", nullable = false)
    private long featurePermissionId;

    @ManyToOne(targetEntity = RoleInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_role_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private RoleInfo roleInfo;

    @ManyToOne(targetEntity = FeaturePermission.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_feature_permission_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private FeaturePermission featurePermission;

    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;

    @Column(name = "created_user_id", nullable = false)
    private BigInteger createdUserId;

    @Column(name = "updated_dt")
    private Timestamp updatedDt;


    @Column(name = "updated_user_id")
    private long updatedUserId;
}
