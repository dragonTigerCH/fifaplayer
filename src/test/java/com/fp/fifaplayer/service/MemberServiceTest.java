package com.fp.fifaplayer.service;

import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.form.MemberSaveForm;
import com.fp.fifaplayer.repository.MemberRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;



   @Test
   @DisplayName("회원 저장")
   public void memberSave() throws Exception {
       //given
       MemberSaveForm memberSaveForm1 = new MemberSaveForm("yoho98@daum.net","chlalsrb","테스트1","ROLE_USER");
       MemberSaveForm memberSaveForm2 = new MemberSaveForm("yoho97@daum.net","chlalsrb","테스트2","ROLE_USER");
       //when
       memberService.save(memberSaveForm1);
       memberService.save(memberSaveForm2);

       Member member1 = memberRepository.findByEmail(memberSaveForm1.getEmail()).orElse(null);
       Member member2 = memberRepository.findByEmail(memberSaveForm2.getEmail()).orElse(null);
       //then
       assertThat(memberSaveForm1.getEmail()).isEqualTo(member1.getEmail());
       assertThat(memberSaveForm2.getEmail()).isEqualTo(member2.getEmail());
   }

    @Test
    @DisplayName("이메일 중복 체크")
    public void 이메일_중복_체크() throws Exception {
        //given
        MemberSaveForm memberSaveForm = new MemberSaveForm("yoho98@daum.net","chlalsrb","테스트1","ROLE_USER");
        memberService.save(memberSaveForm);
        //when
       String falseEmail = memberService.emailCheck("false@daum.net");
       String trueEmail = memberService.emailCheck(memberSaveForm.getEmail());
        //then
       assertThat(falseEmail).isEqualTo("0");
       assertThat(trueEmail).isEqualTo("1");
    }
    @Test
    @DisplayName("닉네임 중복 체크")
    public void 닉네임_중복_체크() throws Exception {
        //given
        MemberSaveForm memberSaveForm = new MemberSaveForm("yoho98@daum.net","chlalsrb","테스트1","ROLE_USER");
        memberService.save(memberSaveForm);
        //when
        String falseNickname = memberService.nicknameCheck("없는닉네임");
        String trueNickname = memberService.nicknameCheck(memberSaveForm.getNickname());
        //then
        assertThat(falseNickname).isEqualTo("0");
        assertThat(trueNickname).isEqualTo("1");
    }
    @Test
    @DisplayName("비밀번호 값 비교")
    public void 비밀번호_값_비교() throws Exception {
        //given
        String password = passwordEncoder.encode("chlalsrb");
        Member member = new Member(null,"yoho98@daum.net",password,"테스트1",null,null,"ROLE_USER");
        memberRepository.save(member);
        String testPassword = "chlalsrb";
        //when
        boolean passwordCheck = memberService.passwordCheck(member.getId(), testPassword);
        //then
        assertTrue(passwordCheck);
    }
    @Test
    @DisplayName("회원 탈퇴")
    public void DeleteMember() throws Exception {
        //given
        Member member = new Member(null,"yoho98@daum.net","chlalsrb","테스트1",null,null,"ROLE_USER");
        Member savedMember = memberRepository.save(member);
        //when
        memberService.memberWithdrawal(savedMember.getId());
        Member findMember = memberRepository.findById(savedMember.getId()).orElse(null);

        //then
        assertEquals(findMember,null,"회원 탈퇴 실패.");

    }





}