package com.fp.fifaplayer.service;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.domain.*;
import com.fp.fifaplayer.form.BoardForm;
import com.fp.fifaplayer.form.Board_CommentsForm;
import com.fp.fifaplayer.form.Board_CommentsUpdateForm;
import com.fp.fifaplayer.repository.BoardRepository;
import com.fp.fifaplayer.repository.Board_CommentsRepository;
import com.fp.fifaplayer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final Board_CommentsRepository board_commentsRepository;


    public Board save(BoardForm boardForm, String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        return boardRepository.save(Board.builder()
                .title(boardForm.getTitle())
                .content(boardForm.getContent())
                .type(boardForm.getType())
                .regdate(LocalDateTime.now())
                .hit(0L)
                .recommend(0L)
                .member(member).build());
    }

    public Page<Board> myWritingBoards(Long member_id, Pageable pageable) {
        return boardRepository.findMyWritingByMember_id(member_id, pageable);
    }



    public Board_Comments commentsSave(Long commentId, Board_CommentsForm board_commentsForm, PrincipalDetails principalDetails) {

        Board board = boardRepository.findById(board_commentsForm.getBoard_id()).orElseThrow(() -> new IllegalArgumentException("작성할 게시글이 없습니다."));
        //댓글 그룹번호 NVL 함수 NULL이면 0  NULL이아니면 최대값
        Long commentsRef = board_commentsRepository.findByNvlRef(board_commentsForm.getBoard_id());
        //commentId가 null 이면 댓글, null이 아니면 대댓글 저장
        if (commentId == null) { //댓글 저장

            Board_Comments savedComment = board_commentsRepository.save(Board_Comments.builder()
                    .content(board_commentsForm.getContent())
                    .regdate(LocalDateTime.now())
                    .member(principalDetails.getMember())
                    .board(board)
                    .ref(commentsRef + 1l)
                    .step(0l)
                    .refOrder(0l)
                    .answerNum(0l)
                    .parentNum(0l)
                    .build());

            return savedComment;

        } else {
            //대댓글 저장
            //부모 댓글 데이터
            Board_Comments board_comments = board_commentsRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("작성할 답글이 없습니다."));
            //refOrderAndUpdate 메소드 실행후 값 가져오기
            Long refOrderResult = refOrderAndUpdate(board_comments);
            //null이면 저장실패
            if (refOrderResult == null) {
                return null;
            }


            //대댓글 저장
            Board_Comments savedReply = board_commentsRepository.save(Board_Comments.builder()
                    .content(board_commentsForm.getContent())
                    .regdate(LocalDateTime.now())
                    .member(principalDetails.getMember())
                    .board(board)
                    .ref(board_comments.getRef())
                    .step(board_comments.getStep() + 1l)
                    .refOrder(refOrderResult)
                    .answerNum(0l)
                    .parentNum(commentId)
                    .build());

            //부모댓글의 자식컬럼수 + 1 업데이트
            board_commentsRepository.updateAnswerNum(board_comments.getId(), board_comments.getAnswerNum());
            return savedReply;
        }
    }

    private Long refOrderAndUpdate(Board_Comments board_comments) {

        Long saveStep = board_comments.getStep() + 1l;
        Long refOrder = board_comments.getRefOrder();
        Long answerNum = board_comments.getAnswerNum();
        Long ref = board_comments.getRef();

        //부모 댓글그룹의 answerNum(자식수)
        Long answerNumSum = board_commentsRepository.findBySumAnswerNum(ref);
        //부모 댓글그룹의 최댓값 step
        Long maxStep = board_commentsRepository.findByNvlMaxStep(ref);

        //저장할 대댓글 step과 그룹내의최댓값 step의 조건 비교
        /*
        step + 1 < 그룹리스트에서 max step값  AnswerNum sum + 1 * NO UPDATE
        step + 1 = 그룹리스트에서 max step값  refOrder + AnswerNum + 1 * UPDATE
        step + 1 > 그룹리스트에서 max step값  refOrder + 1 * UPDATE
        */

        if (saveStep < maxStep) {
            return answerNumSum + 1l;
        } else if (saveStep == maxStep) {
            board_commentsRepository.updateRefOrderPlus(ref, refOrder + answerNum);
            return refOrder + answerNum + 1l;
        } else if (saveStep > maxStep) {
            board_commentsRepository.updateRefOrderPlus(ref, refOrder);
            return refOrder + 1l;
        }

        return null;
    }

    @Transactional
    public void updateComments(Board_CommentsUpdateForm board_commentsUpdateForm) {
        Board_Comments comments = board_commentsRepository.findById(board_commentsUpdateForm.getUpdate_id())
                .orElseThrow(() -> new IllegalStateException("댓글을 찾을 수 없습니다."));

        comments.setContent(board_commentsUpdateForm.getContent());
        comments.setModify_regdate(LocalDateTime.now());
    }

    @Transactional
    public String deleteComments(Long commentId) {
        Board_Comments comments = board_commentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        /* 댓글 대댓글 삭제 로직 */

        /* 자식 댓글이 없다면 */
        if (comments.getAnswerNum() == 0) {
            board_commentsRepository.deleteById(commentId);
            /* 부모 댓글이 있다면 자식수(answerNum) -1 */
                if (comments.getParentNum() != 0) {
                    Board_Comments parentComment = board_commentsRepository.findById(comments.getParentNum())
                            .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

                    Long answerNum = parentComment.getAnswerNum();
                    parentComment.setAnswerNum(answerNum - 1l);
                    /* 부모 댓글의 자식을 삭제 했을때 자식이 0이면서 삭제된 댓글이면 */
                    if (parentComment.getAnswerNum() == 0 && parentComment.isDel_yn() == true) {
                        board_commentsRepository.deleteById(parentComment.getId());
                        return "deleteAll";
                    }
                }
            return "noReply";
        } else {
            /* 자식 댓글이 있다면 */
            comments.setContent("해당 댓글은 삭제되었습니다.");
            comments.setDel_yn(true);
            comments.setModify_regdate(LocalDateTime.now());
            return "containReply";
        }
    }

    public void updateBoardContent(BoardForm boardForm) {
        Board board = boardRepository.findById(boardForm.getId()).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.setTitle(boardForm.getTitle());
        board.setContent(boardForm.getContent());
    }

    public Board deleteBoard(Long deleteId) {
        Board board = boardRepository.findById(deleteId).orElseThrow(() -> new IllegalArgumentException("삭제할 게시글이 없습니다."));
        /* 삭제 로직 - 선택한 게시글의 댓글포함 삭제 */
        board_commentsRepository.deleteByBoard_Id(deleteId);
        boardRepository.deleteById(deleteId);

        return board;
    }

    public Page<Board_Comments> myBoardCommentsLists(Long member_id, Pageable pageable) {
        return board_commentsRepository.findMyBoardCommentsListsByMember_id(member_id, pageable);
    }
}
