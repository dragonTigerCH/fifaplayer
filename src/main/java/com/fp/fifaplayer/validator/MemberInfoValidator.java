package com.fp.fifaplayer.validator;

import com.fp.fifaplayer.form.MemberEditForm;
import com.fp.fifaplayer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



@RequiredArgsConstructor
public class MemberInfoValidator implements Validator{


    private final MemberService memberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberEditForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

            MemberEditForm memberEditForm = (MemberEditForm) target;

             System.out.println(memberService);

            if (memberService.nicknameCheck(memberEditForm.getNickname()).equals("1")){
                errors.rejectValue("nickname",null,"닉네임이 중복입니다.");
            }
            if (!memberService.passwordCheck(memberEditForm.getId(),memberEditForm.getPassword())){
                errors.rejectValue("password",null,"비밀번호가 맞지 않습니다.");
            }

    }
}
