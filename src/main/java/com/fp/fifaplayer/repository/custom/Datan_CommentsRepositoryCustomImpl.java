package com.fp.fifaplayer.repository.custom;


import com.fp.fifaplayer.domain.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository @Primary
@RequiredArgsConstructor
public class Datan_CommentsRepositoryCustomImpl implements Datan_CommentsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QDatan qDatan = QDatan.datan;
    QSeason qSeason = QSeason.season;
    QPlayer qPlayer = QPlayer.player;
    QMember qMember = QMember.member;
    QDatan_Comments qDatan_comments = QDatan_Comments.datan_Comments;

    @Override
    public Page<Datan_Comments> findAllByDatanOrderByCommentsOrRegdate(Long datan_id,Pageable pageable) {

        QueryResults<Datan_Comments>  datan_comments = jpaQueryFactory
                .select(qDatan_comments)
                .from(qDatan_comments)
                .innerJoin(qDatan_comments.datan,qDatan)
                .innerJoin(qDatan_comments.member,qMember)
                .where(qDatan_comments.datan.id.eq(datan_id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qDatan_comments.ref.desc(),
                        qDatan_comments.refOrder.asc())
                .fetchResults();

        List<Datan_Comments> content = datan_comments.getResults();
        long total = datan_comments.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<Datan_Comments> findBySeasonAndPositionAndSearchPlayer(List<String> season, List<String> position, String searchPlayer, Pageable pageable) {

        QueryResults<Datan_Comments> datan_comments = jpaQueryFactory.select(qDatan_comments)
                .from(qDatan_comments)
                .innerJoin(qDatan_comments.datan,qDatan)
                .innerJoin(qDatan_comments.member,qMember)
                .where(inSeasonName(season)
                        ,inPlayerPosition(position)
                        ,qDatan_comments.datan.player.name.contains(searchPlayer))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qDatan_comments.regdate.desc())
                .fetchResults();

        List<Datan_Comments> content = datan_comments.getResults();
        long total = datan_comments.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    private BooleanExpression inSeasonName(List<String> season){
        if(season.isEmpty()){
            return null;
        }
        return qDatan_comments.datan.season.name.in(season);
    }
    private BooleanExpression inPlayerPosition(List<String> position){
        if(position.isEmpty()){
            return null;
        }
        return qDatan_comments.datan.player.position.in(position);
    }

}
