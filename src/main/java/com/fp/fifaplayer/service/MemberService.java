package com.fp.fifaplayer.service;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.form.MemberSaveForm;
import com.fp.fifaplayer.repository.BoardRepository;
import com.fp.fifaplayer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public Long save(MemberSaveForm memberSaveForm) {

        memberSaveForm.setPassword(passwordEncoder.encode(memberSaveForm.getPassword())); //비밀번호 변환

        return memberRepository.save(Member.builder()
                        .email(memberSaveForm.getEmail())
                        .auth(memberSaveForm.getAuth())
                        .nickname(memberSaveForm.getNickname())
                        .password(memberSaveForm.getPassword())
                        .build())
                .getId();
    }


    public String emailCheck(String email) {

        String checkEmail = "0";
        if (memberRepository.findByEmail(email).isPresent()) {
            checkEmail = "1";
        }
        return checkEmail;
    }

    public String nicknameCheck(String nickname) {
        String checkNickname = "0";
        if (memberRepository.findByNickname(nickname).isPresent()) {
            checkNickname = "1";
        }
        return checkNickname;
    }

    public int authEmail(MemberSaveForm memberSaveForm) {
        int num = sendEmail(memberSaveForm.getEmail());
        return num;
    }

    public int sendEmail(String findMember) {

        int num = transmitEmail(findMember);
        return num;

    }


    @Transactional
    public void updatePassword(String newPw, String findMember) {
        Member member = memberRepository.findByEmail(findMember).orElseThrow(() -> {
            return new IllegalArgumentException("비밀번호 변경 실패");
        });
        //새로운 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(newPw));


    }


    @Transactional
    public Member editNickname(PrincipalDetails principalDetails, String nickname) {
        Member member = memberRepository.findByEmail(principalDetails.getMember().getEmail()).orElseThrow(() -> {
            return new IllegalArgumentException("회원찾기 실패");
        });
        member.setNickname(nickname);
        return member;


    }

    public boolean passwordCheck(Long memberId, String inputPassword) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("로그인한 사용자를 찾을 수 없습니다."));
        String dbPassword = member.getPassword();
        return passwordEncoder.matches(inputPassword, dbPassword);
    }


    private int transmitEmail(String findMember) {
        Random r = new Random();
        int num = r.nextInt(999999); // 랜덤난수설정
        System.out.println("num : " + num);
        String setfrom = "yoho9908@gmail.com";
        String tomail = findMember; // 받는사람
        String title = "[FP] 이메일 인증메일 입니다";
        String content = System.getProperty("line.separator") + "<h3>안녕하세요 </h3>"
                + System.getProperty("line.separator") + "FP 이메일 인증번호는 <strong>" + num
                + "</strong> 입니다." + System.getProperty("line.separator"); //

        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(setfrom);
            mimeMessageHelper.setTo(tomail);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(content, true);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return num;
    }

    @Transactional
    public void memberWithdrawal(Long member_id) {
        memberRepository.deleteById(member_id);
    }
}
