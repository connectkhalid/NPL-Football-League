package com.nfl.national_football_league.domain;

import com.nfl.national_football_league.constant.TeamCategory;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "team_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"team_name"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "FK_account_info_id", nullable = false)
    private long accountInfoId;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "team_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamCategory teamCategory;

    @Column(name = "team_owner", nullable = false)
    private String teamOwner;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "image_path", nullable = true)
    private String imagePath;

    @Column(name = "tires", nullable = false)
    private int tires;

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

    @ManyToOne
    @JoinColumn(name = "league_id", nullable = true)
    private LeagueInfo leagueInfo;
    @ManyToOne
    @JoinColumn(name = "club_id", nullable = true)
    private ClubInfo clubInfo;

    @OneToMany(mappedBy = "teamInfo", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<PlayerInfo> players;

    @ManyToMany
    @JoinTable(
            name = "team_season",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "seasonId")
    )
    private Set<SeasonInfo> assignedSeasons = new HashSet<>();
}
