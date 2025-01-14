package com.nfl.national_football_league.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "main_feature_info")
@Data
@NoArgsConstructor
public class MainFeatureInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "main_feature_name", nullable = false)
    private String mainFeatureName;

    @Column(name = "main_feature_code", nullable = false, unique = true)
    private String mainFeatureCode;

    @Column(name = "delete_flg", nullable = false)
    private boolean deleteFlag;

    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;

    @Column(name = "created_user_id", nullable = false)
    private BigInteger createdUserId;
}
