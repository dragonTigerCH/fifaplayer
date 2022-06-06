package com.fp.fifaplayer.form;

import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.service.MemberService;
import lombok.*;
import lombok.extern.java.Log;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditForm {

    private Long id;
    private String email;
    private LocalDateTime createdDate;
    private String auth;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 3,max = 8,message = "닉네임은 3자리부터 8자리까지입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,8}$",message = "닉네임 유형이 맞지않습니다.")
    private String nickname;
    private String password;

    public MemberEditForm(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.createdDate = member.getCreatedDate();
        this.auth = member.getAuth();
        this.nickname = member.getNickname();
    }
}
