package com.fp.fifaplayer.form;

import com.fp.fifaplayer.domain.Member;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardForm {

        private Long id;
        @NotBlank(message = "공백은 허용하지 않습니다.")
        @Size(min = 2,max = 30,message = "제목은 2자이상 30자 이하입니다.")
        private String title;
        private String content;
        private String type;


}
