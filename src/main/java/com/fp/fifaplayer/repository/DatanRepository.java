package com.fp.fifaplayer.repository;

import com.fp.fifaplayer.domain.Datan;
import com.fp.fifaplayer.repository.custom.DatanRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DatanRepository extends JpaRepository<Datan,Long> , DatanRepositoryCustom {


    Datan findByPlayerIdAndSeasonId(Long PlayerId, Long SeasonId);


    @Query(value = "SELECT d.* FROM DATAN d join Season s on s.id = d.season_id join Player p on d.player_id = p.id where d.id in (:id) order by d.overall desc",nativeQuery = true)
    List<Datan> findByIdOrderByDesc(@Param(value = "id") String datanId);

    @Query(value = "SELECT ROUND(AVG(SCORE*1.0),1) RESULT,D.* FROM RATING R JOIN DATAN D ON R.DATAN_ID = D.ID  GROUP BY R.DATAN_ID ORDER BY RESULT DESC",nativeQuery = true)
    List<Datan> averageOrderByList();

    List<Datan> findTop10ByOrderByOverallDesc();
}
