package com.fp.fifaplayer.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Datan_CommentsForm {


    private Long datan_id;
    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;
}
