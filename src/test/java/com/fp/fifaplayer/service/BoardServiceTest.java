package com.fp.fifaplayer.service;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.domain.Board;
import com.fp.fifaplayer.domain.Board_Comments;
import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.form.BoardForm;
import com.fp.fifaplayer.form.Board_CommentsForm;
import com.fp.fifaplayer.form.Board_CommentsUpdateForm;
import com.fp.fifaplayer.repository.BoardRepository;
import com.fp.fifaplayer.repository.Board_CommentsRepository;
import com.fp.fifaplayer.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    Board_CommentsRepository board_commentsRepository;
    @Autowired
    EntityManager em;



    @Test
    @DisplayName("게시글 저장")
    public void boardSave() throws Exception {

        //given
        BoardForm boardForm = new BoardForm(null, "테스트 글1", "ㅋㅋㅋㅋ", "tip");
        Member member = new Member(null, "yoho98@daum.net", "chlalsrb", "테스트1", null, null, "ROLE_USER");
        memberRepository.save(member);
        //when
        Board board = boardService.save(boardForm, member.getEmail());
        Board findBoard = boardRepository.findById(board.getId()).orElse(null);
        //then
        assertEquals(boardForm.getTitle(), findBoard.getTitle());
    }

    @Test
    @DisplayName("내가 쓴 게시글")
    public void myWritingBoards() throws Exception {

        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Member member = new Member(null, "yoho98@daum.net", "chlalsrb", "테스트1", null, null, "ROLE_USER");
        BoardForm boardForm = new BoardForm(null, "테스트 글1", "ㅋㅋㅋㅋ", "tip");
        memberRepository.save(member);
        Board board = boardService.save(boardForm, member.getEmail());
        //when
        Page<Board> boards = boardService.myWritingBoards(member.getId(), pageable);

        //then
        assertEquals(boards.getTotalPages(), 1);
    }

    @Test
    @DisplayName("게시글 댓글, 대댓글 저장")
    public void commentsSave() throws Exception {

        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Member member = new Member(null, "yoho98@daum.net", "chlalsrb", "테스트1", null, null, "ROLE_USER");
        BoardForm boardForm = new BoardForm(null, "테스트 글1", "ㅋㅋㅋㅋ", "tip");
        memberRepository.save(member);
        Board board = boardService.save(boardForm, member.getEmail());
        Board_CommentsForm board_commentsForm = new Board_CommentsForm(board.getId(), "테스트 글1의 댓글이다.");
        Board_CommentsForm board_commentsForm2 = new Board_CommentsForm(board.getId(), "테스트 글1의 대댓글이다.");

        //when
        //댓글
        Board_Comments comments1 = boardService.commentsSave(null, board_commentsForm, new PrincipalDetails(member));
        //대댓글
        Board_Comments comments2 = boardService.commentsSave(comments1.getId(), board_commentsForm2, new PrincipalDetails(member));
        //then
        assertEquals(comments1.getContent(), board_commentsForm.getContent());
        assertEquals(comments2.getContent(), board_commentsForm2.getContent());
    }

    @Test
    @DisplayName("게시글 댓글, 대댓글 수정")
    public void updateComments() throws Exception {

        //given
        Member member = new Member(null, "yoho98@daum.net", "chlalsrb", "테스트1", null, null, "ROLE_USER");
        BoardForm boardForm = new BoardForm(null, "테스트 글1", "ㅋㅋㅋㅋ", "tip");
        memberRepository.save(member);
        Board board = boardService.save(boardForm, member.getEmail());
        Board_CommentsForm board_commentsForm = new Board_CommentsForm(board.getId(), "테스트 글1의 댓글이다.");
        Board_CommentsForm board_commentsForm2 = new Board_CommentsForm(board.getId(), "테스트 글1의 대댓글이다.");
        //댓글 저장
        Board_Comments comments1 = boardService.commentsSave(null, board_commentsForm, new PrincipalDetails(member));
        Board_Comments comments2 = boardService.commentsSave(comments1.getId(), board_commentsForm2, new PrincipalDetails(member));

        String comment = comments1.getContent();
        String reply = comments2.getContent();
        //when
        Board_CommentsUpdateForm updateComment1 = new Board_CommentsUpdateForm(comments1.getId(), "테스트 글1의 댓글 수정");
        Board_CommentsUpdateForm updateComment2 = new Board_CommentsUpdateForm(comments2.getId(), "테스트 글1의 대댓글 수정");

        boardService.updateComments(updateComment1);
        boardService.updateComments(updateComment2);

        Board_Comments board_comments1 = board_commentsRepository.findById(comments1.getId()).orElse(null);
        Board_Comments board_comments2 = board_commentsRepository.findById(comments2.getId()).orElse(null);
        //then
        assertNotEquals(comment, board_comments1.getContent());
        assertNotEquals(reply, board_comments2.getContent());
    }
    @Test
    @DisplayName("게시글 댓글, 대댓글 삭제")
    public void deleteComments() throws Exception {
        //given
        Member member = new Member(null, "yoho98@daum.net", "chlalsrb", "테스트1", null, null, "ROLE_USER");
        BoardForm boardForm = new BoardForm(null, "테스트 글1", "ㅋㅋㅋㅋ", "tip");
        memberRepository.save(member);
        Board board = boardService.save(boardForm, member.getEmail());
        Board_CommentsForm board_commentsForm = new Board_CommentsForm(board.getId(), "테스트 글1의 댓글이다.");
        Board_CommentsForm board_commentsForm2 = new Board_CommentsForm(board.getId(), "테스트 글1의 대댓글이다.");
        //댓글 저장
        Board_Comments comments1 = boardService.commentsSave(null, board_commentsForm, new PrincipalDetails(member));
        Board_Comments comments2 = boardService.commentsSave(comments1.getId(), board_commentsForm2, new PrincipalDetails(member));
        //when
        //같은 트랜잭션 범위안이라서 비즈니스로직이 오작동 해서 트랜잭션 범위나누기
        em.clear();
        String result = boardService.deleteComments(comments1.getId());

        //then
        assertEquals("containReply",result);
    }


}
