package com.fp.fifaplayer.repository.custom;

import com.fp.fifaplayer.domain.*;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QBoard qBoard = QBoard.board;
    QMember qMember = QMember.member;
    QBoard_Comments qBoard_comments = QBoard_Comments.board_Comments;


    @Override
    public Page<Board_Comments> findByTypeTipAndTitleContaining(String searchText, Pageable pageable) {

       /* QueryResults<Board> boardQueryResults = jpaQueryFactory.selectFrom(qBoard)
                .innerJoin()
*/
        return null;
    }

    @Override
    public Page<Board_Comments> findAllByBoardOrderByCommentsOrRegdate(Long board_id, Pageable pageable) {

        QueryResults<Board_Comments> board_comments = jpaQueryFactory
                .select(qBoard_comments)
                .from(qBoard_comments)
                .innerJoin(qBoard_comments.board, qBoard)
                .innerJoin(qBoard_comments.member, qMember)
                .where(qBoard_comments.board.id.eq(board_id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qBoard_comments.ref.desc(),
                        qBoard_comments.refOrder.asc())
                .fetchResults();

        List<Board_Comments> content = board_comments.getResults();
        long total = board_comments.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


}
