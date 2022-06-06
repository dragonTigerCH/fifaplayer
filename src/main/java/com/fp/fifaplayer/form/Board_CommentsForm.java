package com.fp.fifaplayer.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board_CommentsForm {


    private Long board_id;
    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;
}
