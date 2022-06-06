package com.fp.fifaplayer.domain;

import com.fp.fifaplayer.converter.BooleanToYNConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Datan_Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime regdate;
    private LocalDateTime modify_regdate;
    private Long ref;       //그룹번호
    private Long step;      //ex) 최상위 부모글 0 자식글 1 손자글 2 답변의 계층마다 1씩 증가 Level 자식글이냐 손자글이냐 평가
    private Long refOrder;  //같은 그룹내에서의 순서 최상위부모글은 0
    private Long answerNum; //자식글의 개수
    private Long parentNum; //부모글의 기본키
    @Convert(converter = BooleanToYNConverter.class)
    private boolean del_yn; //댓글 삭제여부


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datan_id")
    private Datan datan;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;




}
