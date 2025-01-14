package com.nfl.national_football_league.repository;

import com.nfl.national_football_league.domain.UploadedFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("UploadedFileInfoRepository")
@Transactional(rollbackFor = Exception.class)
public interface UploadedFileInfoRepository extends JpaRepository<UploadedFileInfo, Long> {

}
