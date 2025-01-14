package com.nfl.national_football_league.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "sub_feature_info")
@Data
@NoArgsConstructor
public class SubFeatureInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "sub_feature_name", nullable = false)
    private String subFeatureName;

    @Column(name = "sub_feature_code", nullable = false, unique = true)
    private String subFeatureCode;

    @Column(name = "FK_main_feature_info_id", nullable = false)
    private long mainFeatureInfoTblId;

    @Column(name = "delete_flg", nullable = false)
    private boolean deleteFlag;

    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;

    @Column(name = "created_user_id", nullable = false)
    private BigInteger createdUserId;

    @ManyToOne(targetEntity = MainFeatureInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_main_feature_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MainFeatureInfo mainFeatureInfo;
}
