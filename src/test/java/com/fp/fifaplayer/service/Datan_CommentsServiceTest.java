package com.fp.fifaplayer.service;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.domain.*;
import com.fp.fifaplayer.form.DatanForm;
import com.fp.fifaplayer.form.Datan_CommentsForm;
import com.fp.fifaplayer.form.Datan_CommentsUpdateForm;
import com.fp.fifaplayer.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class Datan_CommentsServiceTest {

    @Autowired
    DatanService datanService;
    @Autowired
    DatanRepository datanRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    Datan_CommentsService datan_commentsService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    @Autowired
    Datan_CommentsRepository datan_commentsRepository;

    Long savedDatanId = null;
    Member member = null;
    Player mrHong = null;
    Season lol = null;

    @BeforeEach
    void beforeSampleDatan_Comments() throws IOException {
        System.out.println("BeforeEach");
        member = new Member(null,"yoho98@daum.net","chlalsrb","테스트1",null,null,"ROLE_USER");
        mrHong = new Player(null,"홍동우",183,69,"마름","1994.01.05","한국","DF");
        lol = new Season(null,"LOL",null);
        MockMultipartFile file = new MockMultipartFile("img_file",
                    "test.png",
                    "image/png",
                    new FileInputStream("C:\\fifaPrj\\fifaplayer\\src\\main\\resources\\static\\img\\배경.png"));
        DatanForm datanForm = new DatanForm(4,90,90,90,90,90,90,90,"토트넘", mrHong.getName(), lol.getName(), file);
        playerRepository.save(mrHong);
        seasonRepository.save(lol);
        memberRepository.save(member);
        savedDatanId = datanService.save(datanForm);
    }
    @AfterEach
    void afterSampleDatan_Comments(){
        System.out.println("AfterEach");
    }



    @Test
    @DisplayName("선수 댓글,대댓글 저장")
    void save() {
        //given
        Datan datan = datanRepository.findById(savedDatanId).orElse(null);
        Datan_CommentsForm commentForm = new Datan_CommentsForm(datan.getId(), "테스트 댓글1");
        Datan_CommentsForm replyForm = new Datan_CommentsForm(datan.getId(), "테스트 댓글1의 댓글");
        //테스트
        //when
        //댓글
        Datan_Comments comment = datan_commentsService.save(null, commentForm, new PrincipalDetails(member));

        //대댓글
        Datan_Comments reply = datan_commentsService.save(comment.getId(), replyForm, new PrincipalDetails(member));

        //then
        assertEquals(comment.getContent(),commentForm.getContent());
        assertEquals(reply.getContent(),replyForm.getContent());

    }

    @Test
    @DisplayName("내가 쓴 선수 댓글")
    void myComments() {
        //given
        Datan datan = datanRepository.findById(savedDatanId).orElse(null);
        Datan_CommentsForm commentForm = new Datan_CommentsForm(datan.getId(), "테스트 댓글1");
        Datan_CommentsForm replyForm = new Datan_CommentsForm(datan.getId(), "테스트 댓글1의 댓글");
        Datan_Comments comment = datan_commentsService.save(null, commentForm, new PrincipalDetails(member));
        Datan_Comments reply = datan_commentsService.save(comment.getId(), replyForm, new PrincipalDetails(member));

        //when
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").descending());
        Page<Datan_Comments> datan_comments = datan_commentsService.myComments(member.getId(), pageable);

        //then
        assertEquals(datan_comments.getTotalElements(),2);

    }

    @Test
    @DisplayName("선수 댓글 수정")
    void updateComments() {
        //given
        Datan datan = datanRepository.findById(savedDatanId).orElse(null);
        Datan_CommentsForm commentForm = new Datan_CommentsForm(datan.getId(), "테스트 댓글1");
        Datan_CommentsForm replyForm = new Datan_CommentsForm(datan.getId(), "테스트 댓글1의 댓글");
        Datan_Comments comment = datan_commentsService.save(null, commentForm, new PrincipalDetails(member));
        Datan_Comments reply = datan_commentsService.save(comment.getId(), replyForm, new PrincipalDetails(member));

        Datan_CommentsUpdateForm updateComment = new Datan_CommentsUpdateForm(comment.getId(), "테스트 댓글1 수정");
        Datan_CommentsUpdateForm updateReply = new Datan_CommentsUpdateForm(reply.getId(), "테스트 댓글1의 댓글 수정");
        //when
        datan_commentsService.updateComments(updateComment);
        datan_commentsService.updateComments(updateReply);
        em.flush();
        em.clear();
        Datan_Comments resultComment = datan_commentsRepository.findById(comment.getId()).orElse(null);
        Datan_Comments resultReply = datan_commentsRepository.findById(reply.getId()).orElse(null);

        //then
        assertEquals(updateComment.getContent(),resultComment.getContent());
        assertEquals(updateReply.getContent(),resultReply.getContent());
    }

    @Test
    @DisplayName("선수 댓글 삭제")
    void deleteComments() {
        //given
        Datan datan = datanRepository.findById(savedDatanId).orElse(null);
        Datan_CommentsForm commentForm = new Datan_CommentsForm(datan.getId(), "테스트 댓글1");
        Datan_CommentsForm replyForm = new Datan_CommentsForm(datan.getId(), "테스트 댓글1의 댓글");
        Datan_Comments comment = datan_commentsService.save(null, commentForm, new PrincipalDetails(member));
        Datan_Comments reply = datan_commentsService.save(comment.getId(), replyForm, new PrincipalDetails(member));

        //when
        em.flush();
        em.clear();
        String result = datan_commentsService.deleteComments(comment.getId());
        Datan_Comments findComment = datan_commentsRepository.findById(comment.getId()).orElse(null);
        //then
        assertEquals(result,"containReply");
        assertTrue(findComment.isDel_yn());
    }
}