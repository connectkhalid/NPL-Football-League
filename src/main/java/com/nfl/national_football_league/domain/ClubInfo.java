package com.nfl.national_football_league.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "club_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"club_name"})})
@Getter
@Setter
@NoArgsConstructor
public class ClubInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "FK_account_info_id", nullable = false)
    private long accountInfoId;

    @Column(name = "club_name", nullable = false)
    private String clubName;

    @Column(name = "club_owner", nullable = false)
    private String clubOwner;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "tire", nullable = false)
    private String tire;

    @Column(name = "image_path", nullable = true)
    private String imagePath;

    @Column(name = "delete_flg", columnDefinition="tinyint(1) default 0")
    private Boolean deleteFlg;

    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;

    @Column(name = "updated_dt")
    private Timestamp updatedDt;

    @ManyToOne(targetEntity = AccountInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_account_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private AccountInfo accountInfo;

    @OneToMany(mappedBy = "clubInfo", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<TeamInfo> teams;
}
