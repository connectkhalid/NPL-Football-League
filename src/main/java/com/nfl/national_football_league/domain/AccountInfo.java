package com.nfl.national_football_league.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "account_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"mail_address"})})
@Data
public class AccountInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "FK_role_info_id", nullable = false)
    private Long roleInfoId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "joining_dt", nullable = false)
    private Timestamp joiningDt;

    @Column(name = "mail_address", nullable = false, unique = true)
    private String mailAddress;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "delete_flg", columnDefinition="tinyint(1) default 0")
    private Boolean deleteFlg;

    @Column(name = "created_user_id")
    private long createdUserId;

    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;

    @Column(name = "updated_dt")
    private Timestamp updatedDt;

    @Column(name = "last_login_dt")
    private Timestamp lastLoginDt;

    @ManyToOne(targetEntity = RoleInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_role_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private RoleInfo roleInfo;

    @OneToMany(targetEntity = TeamInfo.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "accountInfo", orphanRemoval = true)
    private List<TeamInfo> teamInfo;

    @OneToMany(targetEntity = AccessKeyInfo.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "accountInfo", orphanRemoval = true)
    private List<AccessKeyInfo> accessKeyInfos;


}
