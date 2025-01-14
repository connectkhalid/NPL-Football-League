package com.nfl.national_football_league.domain;

import com.nfl.national_football_league.constant.ImageUploadCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "uploaded_file_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@Getter
@Setter
@NoArgsConstructor
public class UploadedFileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageUploadCategory fileCategory;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag;

    @Column(name = "created_dt", nullable = false)
    private Timestamp createdDt;

    @Column(name = "update_dt")
    private Timestamp updatedDt;
}
