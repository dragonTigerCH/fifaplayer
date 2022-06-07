package com.fp.fifaplayer.repository;


import com.fp.fifaplayer.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitle(String title);

    List<Board> findByTitleOrContent(String title, String content);

    Page<Board> findByTypeAndTitleContaining(String type, String title, Pageable pageable);

    Page<Board> findMyWritingByMember_id(Long id, Pageable pageable);

    List<Board> findTop7ByOrderByRegdateDesc();
}
