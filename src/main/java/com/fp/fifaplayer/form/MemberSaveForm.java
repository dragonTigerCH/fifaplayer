package com.fp.fifaplayer.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSaveForm {


    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",message = "이메일 유형이 맞지않습니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8,max = 16, message = "비밀번호는 8자리부터 16자리까지 입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[`~!@$!%*#^?&\\(\\)\\-_=+])(?!.*[^a-zA-z0-9`~!@$!%*#^?&\\(\\)\\-_=+]).{8,16}$",
            message = "영문자,숫자,특수문자를 포함해야합니다.")
    private String password;
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 3,max = 8,message = "닉네임은 3자리부터 8자리까지입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,8}$",message = "닉네임 유형이 맞지않습니다.")
    private String nickname;

    private String auth;



}
