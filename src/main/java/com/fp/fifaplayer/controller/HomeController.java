package com.fp.fifaplayer.controller;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.config.oauth.PrincipalOauth2UserService;
import com.fp.fifaplayer.domain.Board;
import com.fp.fifaplayer.domain.Board_Comments;
import com.fp.fifaplayer.domain.Datan;
import com.fp.fifaplayer.domain.Datan_Comments;
import com.fp.fifaplayer.repository.*;
import com.fp.fifaplayer.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final DatanRepository datanRepository;
    private final Datan_CommentsRepository datan_commentsRepository;
    private final BoardRepository boardRepository;
    private final Board_CommentsRepository board_commentsRepository;


    @GetMapping("/")
    public String index(Model model){

        //평균에서 제일 높은 순으로 데이터 가져오기
        List<Datan> datans = datanRepository.averageOrderByList();
        //평균점수 입력된 데이터가 10개미만이면 overall 순으로 10개 가져오기
        if (datans.size() < 10){
           datans = datanRepository.findTop10ByOrderByOverallDesc();
        }
        //실시간 선수평가
        List<Datan_Comments> datan_comments = datan_commentsRepository.findTop7ByOrderByRegdateDesc();
        //실시간 게시판
        List<Board> boardList = boardRepository.findTop7ByOrderByRegdateDesc();
        //실시간 게시글 댓글
        List<Board_Comments> board_comments = board_commentsRepository.findTop7ByOrderByRegdateDesc();


        model.addAttribute("boardList",boardList);
        model.addAttribute("datans",datans);
        model.addAttribute("board_comments",board_comments);
        model.addAttribute("datan_comments",datan_comments);
        return "/inc/index";
    }

    @GetMapping("/forgot_password")
    public String forgot_password(){
        return "/procedure/forgot_password";
    }

    @PostMapping("/forgot_password")
    public String sentEmail(String findMember, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

        if (!memberRepository.findByEmail(findMember).isPresent()){
            redirectAttributes.addFlashAttribute("msg","가입되어있지 않은 이메일 입니다.");
            return "redirect:/forgot_password";
        }

        int num = memberService.sendEmail(findMember);

        model.addAttribute("findMember",findMember);
        session.setAttribute("num",num);
        session.setMaxInactiveInterval(60*5);

        return "/procedure/sent_email";
    }

    @PostMapping("/sent_email")
    public String sent_email(HttpSession session,Integer checkNum,String findMember,RedirectAttributes redirectAttributes,Model model){

        Object realNum = (Integer) session.getAttribute("num");

        if(realNum == null){
            model.addAttribute("authFalse","인증 유효시간이 초과 되었습니다.");
            model.addAttribute("findMember",findMember);
            redirectAttributes.addFlashAttribute("msg","인증 유효시간이 초과 되었습니다.");
            return "redirect:/forgot_password";
       } else if(realNum.equals(checkNum)){
            model.addAttribute("findMember",findMember);
            return "/procedure/new_password";
        } else {
            model.addAttribute("findMember",findMember);
            model.addAttribute("authFalse","인증번호가 맞지않습니다.");
            return "/procedure/sent_email";
       }
    }
    @PostMapping("/new_password")
    public String new_password(String newPw, String newPwcheck, String findMember, Model model){
        //비밀번호 정규식
        String regexp = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[`~!@$!%*#^?&\\(\\)\\-_=+])(?!.*[^a-zA-z0-9`~!@$!%*#^?&\\(\\)\\-_=+]).{8,16}$";
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(newPw);

        if(newPw.equals(newPwcheck) && m.matches()){
            memberService.updatePassword(newPw,findMember);
            return "redirect:/login";
        } else {
            model.addAttribute("findMember",findMember);
            model.addAttribute("authFalse","비밀번호가 같지않거나 비밀번호 조건이 틀립니다.");
            return "/procedure/new_password";
        }
    }
    @GetMapping("/login")
    public String login(){
        return "/procedure/login";
    }


    @GetMapping("/member/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return "user";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping("/err")
    public void err(HttpServletResponse response) throws IOException {
        response.sendError(500,"500이다");
    }





}




