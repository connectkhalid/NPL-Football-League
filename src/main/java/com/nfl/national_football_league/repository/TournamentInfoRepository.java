package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.TournamentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository("tournamentInfoRepository")
@Transactional(rollbackFor = Exception.class)
public interface TournamentInfoRepository extends JpaRepository<TournamentInfo, Long> {
    Optional<TournamentInfo> findByTournamentName(String tournamentName);

    Optional<TournamentInfo> findById(long tournamentId);
}
