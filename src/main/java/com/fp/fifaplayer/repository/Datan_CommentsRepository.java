package com.fp.fifaplayer.repository;

import com.fp.fifaplayer.domain.Datan_Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface Datan_CommentsRepository extends JpaRepository<Datan_Comments,Long> {

    List<Datan_Comments> findAllBydatan_id(Long datan_id);

    List<Datan_Comments> findAllByRef(Long ref);

    @Query(value = "SELECT NVL(MAX(ref),0) FROM DATAN_COMMENTS d WHERE d.datan_id = ?1",nativeQuery = true)
    Long findByNvlRef(Long datan_id);

    @Query(value = "SELECT MAX(step) FROM DATAN_COMMENTS WHERE ref = ?1",nativeQuery = true)
    Long findByNvlMaxStep(Long ref);

    @Query(value = "SELECT SUM(answerNum) FROM DATAN_COMMENTS WHERE ref = ?1",nativeQuery = true)
    Long findBySumAnswerNum(Long ref);


    @Modifying
    @Query(value = "UPDATE DATAN_COMMENTS SET refOrder = refOrder + 1 WHERE ref = ?1 AND refOrder > ?2",nativeQuery = true)
    void updateRefOrderPlus(long ref,long refOrder);


    @Modifying
    @Query(value = "UPDATE DATAN_COMMENTS SET answerNum = ?2 + 1 WHERE id = ?1",nativeQuery = true)
    void updateAnswerNum(Long id, long answerNum);


    Page<Datan_Comments> findMyCommentsByMember_id(Long member_id, Pageable pageable);

    List<Datan_Comments> findTop7ByOrderByRegdateDesc();


}
