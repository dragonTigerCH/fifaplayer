package com.fp.fifaplayer.controller;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.domain.Board;
import com.fp.fifaplayer.domain.Board_Comments;
import com.fp.fifaplayer.domain.Datan_Comments;
import com.fp.fifaplayer.form.*;
import com.fp.fifaplayer.repository.BoardRepository;
import com.fp.fifaplayer.repository.custom.BoardRepositoryCustom;
import com.fp.fifaplayer.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final BoardRepositoryCustom boardRepositoryCustom;


    @GetMapping("/tip")
    public String tip(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false, defaultValue = "") String searchText) {

        String type = "tip";

        Page<Board> boardTip = boardRepository.findByTypeAndTitleContaining(type, searchText, pageable);

        int startPage = Math.max(1, boardTip.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boardTip.getTotalPages(), boardTip.getPageable().getPageNumber() + 4);

        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);

        model.addAttribute("boardTip", boardTip);
        return "board/tip";
    }

    @GetMapping("/free")
    public String free(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false, defaultValue = "") String searchText) {

        String type = "free";

        Page<Board> boardFree = boardRepository.findByTypeAndTitleContaining(type, searchText, pageable);

        int startPage = Math.max(1, boardFree.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boardFree.getTotalPages(), boardFree.getPageable().getPageNumber() + 4);

        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);

        model.addAttribute("boardFree", boardFree);

        return "board/free";
    }


    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) String choice) {

        model.addAttribute("choice", choice);
        model.addAttribute("boardForm", new BoardForm());

        return "board/form";
    }

    @PostMapping("/form")
    public String formSubmit(@Validated @ModelAttribute BoardForm boardForm, BindingResult bindingResult, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "board/form";
        }

        String memberEmail = authentication.getName();
        boardService.save(boardForm, memberEmail);

        String url = boardForm.getType();

        return "redirect:/board/" + url;
    }

    @GetMapping({"/detail/{boardId}", "/detail"})
    public String detail(Model model, @PathVariable Optional<Long> boardId, @AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 10) Pageable pageable) {

        Long bId = boardId.orElse(0l);
        Board board = boardRepository.findById(bId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Page<Board_Comments> board_comments = boardRepositoryCustom.findAllByBoardOrderByCommentsOrRegdate(board.getId(), pageable);

        int startPage = Math.max(1, board_comments.getPageable().getPageNumber() - 4);
        int endPage = Math.min(board_comments.getTotalPages(), board_comments.getPageable().getPageNumber() + 4);

        model.addAttribute("board", board);
        model.addAttribute("member", principalDetails.getMember());
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("board_comments", board_comments);
        model.addAttribute("board_commentForm", new Board_CommentsForm());


        return "board/detail";
    }

    //댓글  "/detail"   대댓글 "/detail/{commentId}"
    @PostMapping({"/detail/{commentId}", "/detail"})
    public String playerDetailComment(@PathVariable(required = false) Long commentId, @Validated @ModelAttribute Board_CommentsForm board_commentsForm, BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(size = 10) Pageable pageable) {

        if (bindingResult.hasErrors()) {
            log.info("errors{}", bindingResult);
            Page<Board_Comments> board_comments = boardRepositoryCustom.findAllByBoardOrderByCommentsOrRegdate(board_commentsForm.getBoard_id(), pageable);
            model.addAttribute("board_comments", board_comments);
            return "/dataninfo/player :: datan_commentList";
        }

        Board_Comments savedReply = boardService.commentsSave(commentId, board_commentsForm, principalDetails);
        if (savedReply == null) {
            throw new RuntimeException("대댓글 작성이 실패하였습니다.");
        }

        Board board = boardRepository.findById(board_commentsForm.getBoard_id()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Page<Board_Comments> board_comments = boardRepositoryCustom.findAllByBoardOrderByCommentsOrRegdate(board_commentsForm.getBoard_id(), pageable);
        int startPage = Math.max(1, board_comments.getPageable().getPageNumber() - 4);
        int endPage = Math.min(board_comments.getTotalPages(), board_comments.getPageable().getPageNumber() + 4);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("board", board);
        model.addAttribute("member", principalDetails.getMember());
        model.addAttribute("board_comments", board_comments);
        model.addAttribute("board_commentForm", board_commentsForm);

        return "board/detail :: board_commentList";
    }

    //댓글,대댓글 업데이트
    @PostMapping("/comments/update/{commentId}")
    @ResponseBody
    public String updateComments(Board_CommentsUpdateForm board_commentsUpdateForm, Model model) {
        boardService.updateComments(board_commentsUpdateForm);
        return "ok";
    }

    //댓글,대댓글 삭제
    @PostMapping("/comments/delete/{commentId}")
    @ResponseBody
    public String deleteComments(@PathVariable Long commentId, Long datanId, Pageable pageable, Model model, RedirectAttributes redirectAttributes) {

        String result = boardService.deleteComments(commentId);

        return result;
    }


    @GetMapping("/edit")
    public String edit(Model model, Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        model.addAttribute("board", board);
        return "board/edit";
    }

    @PostMapping("/edit")
    public String editUpdate(Model model, BoardForm boardForm) {
        boardService.updateBoardContent(boardForm);
        return "redirect:/board/detail/" + boardForm.getId();
    }

    @PostMapping("/delete")
    public String deleteBoard(Long deleteId) {
        Board board = boardService.deleteBoard(deleteId);

        String url = board.getType();

        return "redirect:/board/" + url;
    }


}
