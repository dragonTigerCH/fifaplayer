package com.fp.fifaplayer.repository.custom;

import com.fp.fifaplayer.domain.Datan;
import com.fp.fifaplayer.domain.Datan_Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Datan_CommentsRepositoryCustom {

    Page<Datan_Comments> findAllByDatanOrderByCommentsOrRegdate(Long datan_id, Pageable pageable);

    Page<Datan_Comments> findBySeasonAndPositionAndSearchPlayer(List<String> season, List<String> position, String searchPlayer, Pageable pageable);
}
