package com.fp.fifaplayer.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board_CommentsUpdateForm {

    Long update_id;
    String content;

}
