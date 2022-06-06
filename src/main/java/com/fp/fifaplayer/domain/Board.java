package com.fp.fifaplayer.domain;

import lombok.*;
import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String type;
    private Long hit;
    private Long recommend;
    private LocalDateTime regdate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public Board(Long id, String title, String content, String type, Long hit, Long recommend,LocalDateTime regdate, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.hit = hit;
        this.recommend = recommend;
        this.regdate = regdate;
        this.member = member;

    }
}
