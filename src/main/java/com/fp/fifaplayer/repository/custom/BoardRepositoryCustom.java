package com.fp.fifaplayer.repository.custom;

import com.fp.fifaplayer.domain.Board;
import com.fp.fifaplayer.domain.Board_Comments;
import com.fp.fifaplayer.domain.Datan_Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    Page<Board_Comments> findByTypeTipAndTitleContaining(String searchText, Pageable pageable);

    Page<Board_Comments> findAllByBoardOrderByCommentsOrRegdate(Long board_id, Pageable pageable);

}
