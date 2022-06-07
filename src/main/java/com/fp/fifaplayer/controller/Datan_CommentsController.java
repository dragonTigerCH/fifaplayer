package com.fp.fifaplayer.controller;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.domain.Datan;
import com.fp.fifaplayer.domain.Datan_Comments;
import com.fp.fifaplayer.domain.Rating;
import com.fp.fifaplayer.form.Datan_CommentsForm;
import com.fp.fifaplayer.form.Datan_CommentsUpdateForm;
import com.fp.fifaplayer.form.SearchForm;
import com.fp.fifaplayer.repository.DatanRepository;
import com.fp.fifaplayer.repository.Datan_CommentsRepository;
import com.fp.fifaplayer.repository.RatingRepository;
import com.fp.fifaplayer.repository.custom.Datan_CommentsRepositoryCustom;
import com.fp.fifaplayer.service.Datan_CommentsService;
import com.fp.fifaplayer.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/dataninfo")
public class Datan_CommentsController {

    private final DatanRepository datanRepository;
    private final Datan_CommentsRepositoryCustom datan_commentsRepositoryCustom;
    private final Datan_CommentsService datan_commentsService;
    private final Datan_CommentsRepository datan_commentsRepository;
    private final RatingRepository ratingRepository;
    private final RatingService ratingService;


    @GetMapping({"/player/{playerId}", "/player"})
    public String playerDetail(@PathVariable(required = false) Optional<Long> playerId, Model model, @PageableDefault(size = 10) Pageable pageable
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long datan_id = playerId.orElse(0l);
        Datan datan = datanRepository.findById(datan_id).orElseThrow(() -> new IllegalStateException("존재하지 않는 선수 입니다."));
        Page<Datan_Comments> datan_comments = datan_commentsRepositoryCustom.findAllByDatanOrderByCommentsOrRegdate(datan_id, pageable);

        //평점 가져오기
        List<Rating> ratingList = ratingRepository.findByDatan_id(datan_id);
        double score = ratingService.scoreAverage(ratingList);


        int startPage = Math.max(1, datan_comments.getPageable().getPageNumber() - 4);
        int endPage = Math.min(datan_comments.getTotalPages(), datan_comments.getPageable().getPageNumber() + 4);

        model.addAttribute("member", principalDetails.getMember());
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("datan_comments", datan_comments);
        model.addAttribute("datan", datan);
        model.addAttribute("score", score);
        model.addAttribute("datan_commentForm", new Datan_CommentsForm());
        model.addAttribute("searchForm", new SearchForm());

        return "/dataninfo/player";
    }

    //댓글  "/player"   대댓글 "/player/{commentId}"
    @PostMapping({"/player/{commentId}", "/player"})
    public String playerDetailComment(@PathVariable(required = false) Long commentId, @Validated @ModelAttribute Datan_CommentsForm datan_commentsForm, BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(size = 10) Pageable pageable) {

        if (bindingResult.hasErrors()) {
            log.info("errors{}", bindingResult);
            Page<Datan_Comments> datan_comments = datan_commentsRepositoryCustom.findAllByDatanOrderByCommentsOrRegdate(datan_commentsForm.getDatan_id(), pageable);
            model.addAttribute("datan_comments", datan_comments);
            return "/dataninfo/player :: datan_commentList";
        }

        Datan_Comments savedReply = datan_commentsService.save(commentId, datan_commentsForm, principalDetails);
        if (savedReply == null) {
            throw new RuntimeException("대댓글 작성이 실패하였습니다.");
        }
        Datan datan = datanRepository.findById(datan_commentsForm.getDatan_id()).orElseThrow(() -> new IllegalStateException("존재하지 않는 선수 입니다."));
        Page<Datan_Comments> datan_comments = datan_commentsRepositoryCustom.findAllByDatanOrderByCommentsOrRegdate(datan_commentsForm.getDatan_id(), pageable);
        int startPage = Math.max(1, datan_comments.getPageable().getPageNumber() - 4);
        int endPage = Math.min(datan_comments.getTotalPages(), datan_comments.getPageable().getPageNumber() + 4);

        model.addAttribute("datan", datan);
        model.addAttribute("member", principalDetails.getMember());
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("datan_comments", datan_comments);
        model.addAttribute("datan_commentForm", datan_commentsForm);


        return "/dataninfo/player :: datan_commentList";
    }


    @PostMapping("/comments/update/{commentId}")
    @ResponseBody
    public String updateComments(Datan_CommentsUpdateForm datan_commentsUpdateForm, Model model) {
        System.out.println(datan_commentsUpdateForm);
        datan_commentsService.updateComments(datan_commentsUpdateForm);
        return "ok";
    }

    @PostMapping("/comments/delete/{commentId}")
    @ResponseBody
    public String deleteComments(@PathVariable Long commentId, Long datanId, Pageable pageable, Model model, RedirectAttributes redirectAttributes) {

        System.out.println("플레이어 id = " + datanId);
        String result = datan_commentsService.deleteComments(commentId);

        return result;
    }

}
