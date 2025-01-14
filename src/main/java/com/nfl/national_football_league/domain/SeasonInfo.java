package com.nfl.national_football_league.domain;

import com.nfl.national_football_league.constant.SeasonCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Year;
import java.util.List;

@Entity
@Table(name = "season_info", uniqueConstraints = {@UniqueConstraint(columnNames = { "FK_team_info_id"})})
@Getter
@Setter
@NoArgsConstructor
public class SeasonInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "fK_category_id", nullable = false)
    private long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "season_Category", nullable = false)
    private SeasonCategory seasonCategory;

    @Column(name = "fk_category_name")
    private String categoryName;

    @Column(name = "season_year")
    private Year seasonYear;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "location")
    private String location;

    @Column(name = "FK_account_info_id", nullable = false)
    private long accountInfoId;

    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;

    @Column(name = "updated_dt")
    private Timestamp updatedDt;

    @Column(name = "delete_flg", columnDefinition="tinyint(1) default 0")
    private Boolean deleteFlg;

    @ManyToOne(targetEntity = AccountInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_account_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private AccountInfo accountInfo;

    @ManyToOne(targetEntity = TournamentInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fK_category_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private TournamentInfo tournamentInfo;

    @ManyToOne(targetEntity = LeagueInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fK_category_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private LeagueInfo leagueInfo;

    @ManyToMany(mappedBy = "assignedSeasons")
    private List<TeamInfo> teamList;
}
