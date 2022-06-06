package com.fp.fifaplayer.service;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.domain.Datan;
import com.fp.fifaplayer.domain.Datan_Comments;
import com.fp.fifaplayer.form.Datan_CommentsForm;
import com.fp.fifaplayer.form.Datan_CommentsUpdateForm;
import com.fp.fifaplayer.repository.DatanRepository;
import com.fp.fifaplayer.repository.Datan_CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class Datan_CommentsService {

    private final Datan_CommentsRepository datan_commentsRepository;
    private final DatanRepository datanRepository;

    public Datan_Comments save(Long commentId, Datan_CommentsForm datan_commentsForm, PrincipalDetails principalDetails) {

        Datan datan = datanRepository.findById(datan_commentsForm.getDatan_id()).orElseThrow(() -> new IllegalArgumentException("작성할 선수 데이터가 없습니다."));
        //댓글 그룹번호 NVL 함수 NULL이면 0  널이아니면 최대값
        Long commentsRef = datan_commentsRepository.findByNvlRef(datan_commentsForm.getDatan_id());

        if(commentId == null){ //댓글 저장

            Datan_Comments savedComment = datan_commentsRepository.save(Datan_Comments.builder()
                    .content(datan_commentsForm.getContent())
                    .regdate(LocalDateTime.now())
                    .member(principalDetails.getMember())
                    .datan(datan)
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
            Datan_Comments comments = datan_commentsRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("작성할 답글이 없습니다."));
            //refOrderAndUpdate 메소드 실행후 값 가져오기
            Long refOrderResult = refOrderAndUpdate(comments);
            //null이면 저장실패
            if(refOrderResult == null){
                return null;
            }

            //대댓글 저장
            Datan_Comments savedReply = datan_commentsRepository.save(Datan_Comments.builder()
                    .content(datan_commentsForm.getContent())
                    .regdate(LocalDateTime.now())
                    .member(principalDetails.getMember())
                    .datan(datan)
                    .ref(comments.getRef())
                    .step(comments.getStep() + 1l)
                    .refOrder(refOrderResult)
                    .answerNum(0l)
                    .parentNum(commentId)
                    .build());

            //부모댓글의 자식컬럼수 + 1 업데이트
            datan_commentsRepository.updateAnswerNum(comments.getId(),comments.getAnswerNum());

            return savedReply;

        }

    }

    //대댓글 refOrder 로직실행 메소드
    private Long refOrderAndUpdate(Datan_Comments comment) {

        Long saveStep = comment.getStep() +1l;
        Long refOrder = comment.getRefOrder();
        Long answerNum = comment.getAnswerNum();
        Long ref = comment.getRef();
        //부모 댓글그룹의 answerNum(자식수)
        Long answerNumSum = datan_commentsRepository.findBySumAnswerNum(ref);
        //부모 댓글그룹의 최댓값 step

        Long maxStep = datan_commentsRepository.findByNvlMaxStep(ref);

        //저장할 대댓글 step과 그룹내의최댓값 step의 조건 비교
        /*
        step + 1 < 그룹리스트에서 max step값  AnswerNum sum + 1 * NO UPDATE
        step + 1 = 그룹리스트에서 max step값  refOrder + AnswerNum + 1 * UPDATE
        step + 1 > 그룹리스트에서 max step값  refOrder + 1 * UPDATE
        */
        if(saveStep < maxStep){
            return answerNumSum+1l;
        } else if (saveStep == maxStep){
            datan_commentsRepository.updateRefOrderPlus(ref,refOrder+answerNum);
            return refOrder + answerNum + 1l;
        } else if(saveStep > maxStep) {
            datan_commentsRepository.updateRefOrderPlus(ref,refOrder);
            return refOrder + 1l;
        }

        return null;
    }


    public Page<Datan_Comments> myComments(Long member_id, Pageable pageable) {
        return datan_commentsRepository.findMyCommentsByMember_id(member_id,pageable);
    }

    @Transactional
    public void updateComments(Datan_CommentsUpdateForm datan_commentsUpdateForm) {

        Datan_Comments comments = datan_commentsRepository.findById(datan_commentsUpdateForm.getUpdate_id())
                .orElseThrow(() -> new IllegalStateException("댓글을 찾을 수 없습니다."));

        comments.setContent(datan_commentsUpdateForm.getContent());
        comments.setModify_regdate(LocalDateTime.now());

    }
    @Transactional
    public String deleteComments(Long commentId) {

        Datan_Comments comments = datan_commentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));


        /* 댓글 대댓글 삭제 로직 */

        /* 자식 댓글이 없다면 */
        if (comments.getAnswerNum() == 0){
            datan_commentsRepository.deleteById(commentId);
            /* 부모 댓글이 있다면 자식수(answerNum) -1 */
            if (comments.getParentNum() != 0){
                Datan_Comments parentComment = datan_commentsRepository.findById(comments.getParentNum())
                        .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
                Long answerNum = parentComment.getAnswerNum();
                parentComment.setAnswerNum(answerNum-1l);
                /* 부모 댓글의 자식을 삭제 했을때 자식이 0이면서 삭제된 댓글이면 */
                if (parentComment.getAnswerNum() == 0 && parentComment.isDel_yn() == true){
                    datan_commentsRepository.deleteById(parentComment.getId());
                    return "deleteAll";
                }
            }
            return "noReply";
        } else {
            /* 자식 댓글이 있다면 */
            comments.setContent("해당 댓글은 삭제되었습니다.");
            comments.setDel_yn(true);
            return "containReply";
        }

    }
}
