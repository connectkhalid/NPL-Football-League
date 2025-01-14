package com.nfl.national_football_league.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "access_key_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"access_key", "FK_account_info_id"})})
@ToString(exclude = {"accountInfo"})
@Data
public class AccessKeyInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_key", nullable = false)
    private String accessKey;

    @Column(name = "mail_address", nullable = false)
    private String mailAddress;

    @Column(name = "FK_account_info_id", nullable = false)
    private Long accountInfoId;

    @Column(name = "last_access_dt", nullable = false)
    private Timestamp lastAccessDt;


    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;


    @Column(name = "updated_dt")
    private Timestamp updatedDt;


    @ManyToOne(targetEntity = AccountInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_account_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    private AccountInfo accountInfo;

}
