package com.nfl.national_football_league.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "player_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"mail_address"})})
@Getter
@Setter
@NoArgsConstructor
public class PlayerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "FK_role_info_id", nullable = false)
    private Long roleInfoId;

    @Column(name = "FK_team_info_id" )
    private Long teamInfoId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "mail_address", nullable = false, unique = true)
    private String mailAddress;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private Timestamp dateOfBirth;

    @Column(name = "height", nullable = false)
    private String height;

    @Column(name = "weight", nullable = false)
    private String weight;

    @Column(name = "nid_number", nullable = false)
    private String nidNumber;

    @Column(name = "playing_position", nullable = false)
    private String playingPosition;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "delete_flg", columnDefinition="tinyint(1) default 0")
    private Boolean deleteFlg;

    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;

    @Column(name = "updated_dt")
    private Timestamp updatedDt;

    @Column(name = "last_login_dt")
    private Timestamp lastLoginDt;

    @Column(name = "joining_dt", nullable = false)
    private Timestamp joiningDt;

    @ManyToOne(targetEntity = RoleInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_role_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private RoleInfo roleInfo;

    @ManyToOne
    @JoinColumn(name = "FK_team_info_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false)
    private TeamInfo teamInfo;


    @OneToMany(targetEntity = PlayerAccessKeyInfo.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "playerInfo", orphanRemoval = true)
    private List<PlayerAccessKeyInfo> playerAccessKeyInfos;
}
