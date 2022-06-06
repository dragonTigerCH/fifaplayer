package com.fp.fifaplayer.repository;

import com.fp.fifaplayer.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season,Long> {


    Optional<Season> findByName(String season);
}
