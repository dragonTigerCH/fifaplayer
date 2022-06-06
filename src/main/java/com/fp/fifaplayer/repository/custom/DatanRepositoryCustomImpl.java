package com.fp.fifaplayer.repository.custom;

import com.fp.fifaplayer.domain.*;
import com.fp.fifaplayer.embeddable.QAbility;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
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
public class DatanRepositoryCustomImpl implements DatanRepositoryCustom {


        private final JPAQueryFactory jpaQueryFactory;


        QDatan qDatan = QDatan.datan;
        QSeason qSeason = QSeason.season;
        QPlayer qPlayer = QPlayer.player;
        QAbility qAbility = QAbility.ability;


    @Override //검색페이지
    public Page<Datan> findBySeasonAndPositionAndSearchPlayer(List<String> season, List<String> position, String searchPlayer,Pageable pageable) {
        QueryResults<Datan> datans =  jpaQueryFactory.select(qDatan)
                .from(qDatan)
                .innerJoin(qDatan.season,qSeason)
                .innerJoin(qDatan.player,qPlayer)
                .where(inSeasonName(season)
                        ,inPlayerPosition(position)
                        ,qPlayer.name.contains(searchPlayer))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qDatan.overall.desc())
                .fetchResults();

        List<Datan> content = datans.getResults();
        long total = datans.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    private BooleanExpression inSeasonName(List<String> season){
        if(season.isEmpty()){
            return null;
        }
        return qSeason.name.in(season);
    }
    private BooleanExpression inPlayerPosition(List<String> position){
        if(position.isEmpty()){
            return null;
        }
        return qPlayer.position.in(position);
    }



    @Override //선수 능력치 비교 Desc
    public List<Datan> findByIdOrderByDesc(List<Long> datanIds, String dataInfo) {

        List<Datan> datans = jpaQueryFactory.select(qDatan)
                .from(qDatan)
                .innerJoin(qDatan.season,qSeason)
                .innerJoin(qDatan.player,qPlayer)
                .where(qDatan.id.in(datanIds)).orderBy(dataValueDesc(dataInfo)).fetch();
        return datans;
    }

    @Override //선수 능력치 비교 Asc
    public List<Datan> findByIdOrderByAsc(List<Long> datanIds, String dataInfo) {
        List<Datan> datans = jpaQueryFactory.select(qDatan)
                .from(qDatan)
                .innerJoin(qDatan.season,qSeason)
                .innerJoin(qDatan.player,qPlayer)
                .where(qDatan.id.in(datanIds)).orderBy(dataValueAsc(dataInfo)).fetch();
        return datans;
    }

    private OrderSpecifier<Integer> dataValueDesc(String dataInfo){
        if(dataInfo.equals("d.overall")){
            return qDatan.overall.desc();
        } else if (dataInfo.equals("d.salaryup")){
            return qDatan.ability.salaryup.desc();
        }else if (dataInfo.equals("d.speed")){
            return qDatan.ability.speed.desc();
        }else if (dataInfo.equals("d.shooting")){
            return qDatan.ability.shooting.desc();
        }else if (dataInfo.equals("d.pass")){
            return qDatan.ability.pass.desc();
        }else if (dataInfo.equals("d.dribble")){
            return qDatan.ability.dribble.desc();
        }else if (dataInfo.equals("d.defence")){
            return qDatan.ability.defence.desc();
        }else if (dataInfo.equals("d.physical")){
            return qDatan.ability.physical.desc();
        }else if (dataInfo.equals("d.blocking")){
            return qDatan.ability.blocking.desc();
        }
        return null;
    }
    private OrderSpecifier<Integer> dataValueAsc(String dataInfo){
        if(dataInfo.equals("d.overall")){
            return qDatan.overall.asc();
        } else if (dataInfo.equals("d.salaryup")){
            return qDatan.ability.salaryup.asc();
        }else if (dataInfo.equals("d.speed")){
            return qDatan.ability.speed.asc();
        }else if (dataInfo.equals("d.shooting")){
            return qDatan.ability.shooting.asc();
        }else if (dataInfo.equals("d.pass")){
            return qDatan.ability.pass.asc();
        }else if (dataInfo.equals("d.dribble")){
            return qDatan.ability.dribble.asc();
        }else if (dataInfo.equals("d.defence")){
            return qDatan.ability.defence.asc();
        }else if (dataInfo.equals("d.physical")){
            return qDatan.ability.physical.asc();
        }else if (dataInfo.equals("d.blocking")){
            return qDatan.ability.blocking.asc();
        }
        return null;
    }




}
