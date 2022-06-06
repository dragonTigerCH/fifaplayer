package com.fp.fifaplayer.repository;

import com.fp.fifaplayer.domain.Board_Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Board_CommentsRepository extends JpaRepository<Board_Comments,Long> {

    @Query(value = "SELECT NVL(MAX(ref),0) FROM BOARD_COMMENTS b WHERE b.board_id = ?1",nativeQuery = true)
    Long findByNvlRef(Long board_id);

    @Query(value = "SELECT MAX(step) FROM BOARD_COMMENTS WHERE ref = ?1",nativeQuery = true)
    Long findByNvlMaxStep(Long ref);

    @Query(value = "SELECT SUM(answerNum) FROM BOARD_COMMENTS WHERE ref = ?1",nativeQuery = true)
    Long findBySumAnswerNum(Long ref);

    @Modifying
    @Query(value = "UPDATE BOARD_COMMENTS SET refOrder = refOrder + 1 WHERE ref = ?1 AND refOrder > ?2",nativeQuery = true)
    void updateRefOrderPlus(long ref,long refOrder);

    @Modifying
    @Query(value = "UPDATE BOARD_COMMENTS SET answerNum = ?2 + 1 WHERE id = ?1",nativeQuery = true)
    void updateAnswerNum(Long id, long answerNum);


    void deleteByBoard_Id(Long deleteId);

    Page<Board_Comments> findMyBoardCommentsListsByMember_id(Long member_id, Pageable pageable);

    List<Board_Comments> findTop7ByOrderByRegdateDesc();


}
