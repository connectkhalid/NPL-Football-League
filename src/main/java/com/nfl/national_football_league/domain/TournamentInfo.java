package com.nfl.national_football_league.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tournament_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"tournament_name"})})
@Getter
@Setter
@NoArgsConstructor
public class TournamentInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "FK_account_info_id", nullable = false)
    private long accountInfoId;

    @Column(name = "tournament_name", nullable = false)
    private String tournamentName;

    @Column(name = "tournament_owner", nullable = false)
    private String tournamentOwner;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "tournament_year", nullable = false)
    private Year tournamentYear;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "end_time", nullable = false)
    private Timestamp endTime;

    @Column(name = "image_path", nullable = false)
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

    @OneToMany(targetEntity = SeasonInfo.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "tournamentInfo", orphanRemoval = true)
    private Set<SeasonInfo> seasonList = new HashSet<>();
}