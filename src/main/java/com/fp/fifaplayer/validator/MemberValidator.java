package com.fp.fifaplayer.validator;

import com.fp.fifaplayer.form.MemberSaveForm;
import com.fp.fifaplayer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MemberValidator implements Validator {

    private final MemberService memberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberSaveForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors bindingResult) {
        MemberSaveForm memberSaveForm = (MemberSaveForm) target;

        //이메일 검증 정규식
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(memberSaveForm.getEmail());
        System.out.println(memberSaveForm);
        System.out.println(memberService.emailCheck(memberSaveForm.getEmail()));
        //검증 로직
        if (memberService.emailCheck(memberSaveForm.getEmail()).equals("1")) {
            bindingResult.rejectValue("email", null, "이메일이 중복입니다.");
        }
        if (memberService.nicknameCheck(memberSaveForm.getNickname()).equals("1")) {
            bindingResult.rejectValue("nickname", null, "닉네임이 중복입니다.");
        }
        /*
        if(!StringUtils.hasText(memberForm.getEmail()) || !m.matches()){   // 이메일값에 무언가가 들어있지않거나 이메일 정규식과 이메일의값이 맞지않으면
            bindingResult.reject("email");
        }
        if(memberForm.getNickname() == null || 3 >= memberForm.getNickname().length() || memberForm.getNickname().length() >= 11){
            bindingResult.reject("nickname");
        }
        if(memberForm.getPassword() == null || 8 >= memberForm.getPassword().length() || memberForm.getPassword().length() >= 25){
            bindingResult.reject("password");
        }

         */

    }
}
