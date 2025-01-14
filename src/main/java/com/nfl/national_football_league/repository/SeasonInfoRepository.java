package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.constant.SeasonCategory;
import com.nfl.national_football_league.domain.SeasonInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.Year;
import java.util.Optional;

public interface SeasonInfoRepository extends JpaRepository<SeasonInfo, Long> {
    Optional<SeasonInfo> findBySeasonCategoryAndCategoryNameAndSeasonYearAndStartDateAndEndDate(SeasonCategory seasonCategory, String categoryName, Year seasonYear, Timestamp startTime, Timestamp endTime);
}
