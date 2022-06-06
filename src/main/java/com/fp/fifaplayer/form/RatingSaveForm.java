package com.fp.fifaplayer.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingSaveForm {

    private Long datan_id;

    private int score;

}
