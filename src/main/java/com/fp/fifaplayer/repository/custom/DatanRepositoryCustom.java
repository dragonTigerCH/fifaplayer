package com.fp.fifaplayer.repository.custom;

import com.fp.fifaplayer.domain.Datan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DatanRepositoryCustom {


    Page<Datan> findBySeasonAndPositionAndSearchPlayer(List<String> season, List<String> position, String searchPlayer, Pageable pageable);

    List<Datan> findByIdOrderByDesc(List<Long> datanIds, String dataInfo);

    List<Datan> findByIdOrderByAsc(List<Long> datanIds, String dataInfo);
}
