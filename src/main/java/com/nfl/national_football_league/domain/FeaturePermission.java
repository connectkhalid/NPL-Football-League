package com.nfl.national_football_league.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "feature_permission")
@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class FeaturePermission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(targetEntity = SubFeatureInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_sub_feature_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private SubFeatureInfo subFeatureInfo;

    @ManyToOne(targetEntity = PermissionInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_permission_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private PermissionInfo permissionInfo;

    @Column(name = "FK_sub_feature_info_id", nullable = false)
    private long subFeatureInfoId;

    @Column(name = "FK_permission_info_id", nullable = false)
    private long permissionInfoId;

    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;

    @Column(name = "created_user_id", nullable = false)
    private long createdUserId;

    @Column(name = "updated_dt")
    private Timestamp updatedDt;

    @Column(name = "updated_user_id")
    private long updatedUserId;
}
