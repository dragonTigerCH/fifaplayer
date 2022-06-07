package com.fp.fifaplayer.controller;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.domain.Board;
import com.fp.fifaplayer.domain.Board_Comments;
import com.fp.fifaplayer.domain.Datan_Comments;
import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.form.MemberEditForm;
import com.fp.fifaplayer.form.MemberSaveForm;
import com.fp.fifaplayer.form.NewPasswordForm;
import com.fp.fifaplayer.repository.MemberRepository;
import com.fp.fifaplayer.service.BoardService;
import com.fp.fifaplayer.service.Datan_CommentsService;
import com.fp.fifaplayer.service.MemberService;
import com.fp.fifaplayer.validator.MemberInfoValidator;
import com.fp.fifaplayer.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final Datan_CommentsService datan_commentsService;
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("memberSaveForm", new MemberSaveForm());
        return "member/register";
    }

    @PostMapping("/register")
    public String memberJoin(@Validated @ModelAttribute MemberSaveForm memberSaveForm, BindingResult bindingResult, Model model, HttpSession session) {

        new MemberValidator(memberService).validate(memberSaveForm, bindingResult);

        //검증 실패하면 다시 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "/member/register";
        }
        //성공 로직
        int num = memberService.authEmail(memberSaveForm);
        session.setAttribute("num", num);
        session.setMaxInactiveInterval(60 * 5);
        session.setAttribute("memberSaveForm", memberSaveForm);
        model.addAttribute("memberEmail", memberSaveForm.getEmail());

        return "/member/auth";
    }

    //회원가입 인증
    @PostMapping("/auth")
    public String auth(Integer checkNum, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

        Object realNum = (Integer) session.getAttribute("num");
        MemberSaveForm memberSaveForm = (MemberSaveForm) session.getAttribute("memberSaveForm");

        if (realNum == null) {
            redirectAttributes.addFlashAttribute("msg", "인증 시간이 초과되었습니다.");
            return "reditect:/member/register";
        } else if (realNum.equals(checkNum)) {
            memberService.save(memberSaveForm);
            redirectAttributes.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
            return "redirect:/member/login";
        } else {
            model.addAttribute("memberEmail", memberSaveForm.getEmail());
            model.addAttribute("authFalse", "인증번호가 맞지않습니다.");
            return "/member/auth";
        }
    }

    @GetMapping("/info")
    public String info(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       RedirectAttributes redirectAttributes) {


        Member member = memberRepository.findByEmail(principalDetails.getMember().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인한 유저를 찾을 수 없습니다."));

        Page<Board> myWritingBoards = boardService.myWritingBoards(principalDetails.getMember().getId(), pageable);
        Page<Datan_Comments> myDatan_Comments = datan_commentsService.myComments(principalDetails.getMember().getId(), pageable);
        Page<Board_Comments> myBoard_Comments = boardService.myBoardCommentsLists(principalDetails.getMember().getId(), pageable);

        MemberEditForm memberEditForm = new MemberEditForm(member);

        /*내가 쓴 게시글 페이징*/
        int startBoardPage = Math.max(1, myWritingBoards.getPageable().getPageNumber() - 4);
        int endBoardPage = Math.min(myWritingBoards.getTotalPages(), myWritingBoards.getPageable().getPageNumber() + 4);
        model.addAttribute("startBoardPage", startBoardPage);
        model.addAttribute("endBoardPage", endBoardPage);

        /*내가 쓴 선수 댓글 페이징*/
        int startDatanCommentPage = Math.max(1, myDatan_Comments.getPageable().getPageNumber() - 4);
        int endDatanCommentPage = Math.min(myDatan_Comments.getTotalPages(), myDatan_Comments.getPageable().getPageNumber() + 4);
        model.addAttribute("startDatanCommentPage", startDatanCommentPage);
        model.addAttribute("endDatanCommentPage", endDatanCommentPage);

        /*내가 쓴 게시글 댓글 페이징*/
        int startBoardCommentPage = Math.max(1, myBoard_Comments.getPageable().getPageNumber() - 4);
        int endBoardCommentPage = Math.min(myBoard_Comments.getTotalPages(), myDatan_Comments.getPageable().getPageNumber() + 4);
        model.addAttribute("startBoardCommentPage", startBoardCommentPage);
        model.addAttribute("endBoardCommentPage", endBoardCommentPage);

        model.addAttribute("myWritingBoards", myWritingBoards);
        model.addAttribute("myDatan_Comments", myDatan_Comments);
        model.addAttribute("myBoard_Comments", myBoard_Comments);
        model.addAttribute("memberEditForm", memberEditForm);

        return "/member/info";
    }

    @PostMapping("/info")
    public String EditNicknameForm(@Validated MemberEditForm memberEditForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes
            , @AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        new MemberInfoValidator(memberService).validate(memberEditForm, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);

            Page<Board> myWritingBoards = boardService.myWritingBoards(principalDetails.getMember().getId(), pageable);
            Page<Datan_Comments> myDatan_Comments = datan_commentsService.myComments(principalDetails.getMember().getId(), pageable);
            Page<Board_Comments> myBoard_Comments = boardService.myBoardCommentsLists(principalDetails.getMember().getId(), pageable);

            /*내가 쓴 게시글 페이징*/
            int startBoardPage = Math.max(1, myWritingBoards.getPageable().getPageNumber() - 4);
            int endBoardPage = Math.min(myWritingBoards.getTotalPages(), myWritingBoards.getPageable().getPageNumber() + 4);
            model.addAttribute("startBoardPage", startBoardPage);
            model.addAttribute("endBoardPage", endBoardPage);

            /*내가 쓴 선수 댓글 페이징*/
            int startDatanCommentPage = Math.max(1, myDatan_Comments.getPageable().getPageNumber() - 4);
            int endDatanCommentPage = Math.min(myDatan_Comments.getTotalPages(), myDatan_Comments.getPageable().getPageNumber() + 4);
            model.addAttribute("startDatanCommentPage", startDatanCommentPage);
            model.addAttribute("endDatanCommentPage", endDatanCommentPage);

            /*내가 쓴 게시글 댓글 페이징*/
            int startBoardCommentPage = Math.max(1, myBoard_Comments.getPageable().getPageNumber() - 4);
            int endBoardCommentPage = Math.min(myBoard_Comments.getTotalPages(), myDatan_Comments.getPageable().getPageNumber() + 4);
            model.addAttribute("startBoardCommentPage", startBoardCommentPage);
            model.addAttribute("endBoardCommentPage", endBoardCommentPage);

            model.addAttribute("myWritingBoards", myWritingBoards);
            model.addAttribute("myDatan_Comments", myDatan_Comments);
            model.addAttribute("memberEditForm", memberEditForm);
            model.addAttribute("myBoard_Comments", myBoard_Comments);

            return "/member/info";
        }

        Member member = memberService.editNickname(principalDetails, memberEditForm.getNickname());
        // 시큐리티 세션 재등록
        log.info("유저정보 {}" + principalDetails.getUsername(), principalDetails.getPassword());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getUsername(), memberEditForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        redirectAttributes.addFlashAttribute("msg", "수정되었습니다.");

        return "redirect:/member/info";
    }

    @GetMapping("/new_password")
    public String new_password(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        model.addAttribute("newPasswordForm", new NewPasswordForm());
        model.addAttribute("member", principalDetails.getMember());
        return "/member/new_password";
    }

    @PostMapping("/new_password")
    public String new_passwordInsert(@Validated NewPasswordForm newPasswordForm, BindingResult bindingResult, Model model,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails, RedirectAttributes redirectAttributes) {

        if (!newPasswordForm.getPassword().equals(newPasswordForm.getPasswordCheck())) {
            bindingResult.rejectValue("password", null, "비밀번호가 같지 않습니다.");
        }
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            model.addAttribute("member", principalDetails.getMember());
            return "/member/new_password";
        }

        String newPassword = newPasswordForm.getPassword();
        String memberEmail = principalDetails.getMember().getEmail();
        memberService.updatePassword(newPassword, memberEmail);
        redirectAttributes.addFlashAttribute("msg", "비밀번호가 변경되었습니다.");

        return "redirect:/";
    }

    @PostMapping("/withdrawal")
    public String memberWithdrawal(HttpServletRequest request, HttpServletResponse response,
                                   RedirectAttributes redirectAttributes, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        memberService.memberWithdrawal(principalDetails.getMember().getId());
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        redirectAttributes.addFlashAttribute("msg", "회원 탈퇴가 성공적으로 완료되었습니다.");
        return "redirect:/";
    }


}
