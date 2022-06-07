package com.fp.fifaplayer.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatanForm {

    @NotNull(message = "급여를 입력해주세요.")
    @Range(min = 5, max = 30)
    private Integer salaryup;
    @NotNull(message = "스피드 수치를 입력해주세요.")
    @Range(min = 10, max = 150)
    private Integer speed;
    @NotNull(message = "슛 수치를 입력해주세요.")
    @Range(min = 10, max = 150)
    private Integer shooting;
    @NotNull(message = "패스 수치를 입력해주세요.")
    @Range(min = 10, max = 150)
    private Integer pass;
    @NotNull(message = "드리블 수치를 입력해주세요.")
    @Range(min = 10, max = 150)
    private Integer dribble;
    @NotNull(message = "수비 수치를 입력해주세요.")
    @Range(min = 10, max = 150)
    private Integer defence;
    @NotNull(message = "피지컬 수치를 입력해주세요.")
    @Range(min = 10, max = 150)
    private Integer physical;
    @NotNull(message = "블로킹 수치를 입력해주세요.")
    @Range(min = 10, max = 150)
    private Integer blocking;
    @Pattern(regexp = "^[가-힣\\s]{2,44}$", message = "한글과 공백만 입력가능합니다.")
    @NotBlank(message = "소속클럽을 입력하세요.")
    private String club;

    @NotNull(message = "선수를 선택해주세요.")
    private String player;
    @NotNull(message = "시즌을 선택해주세요.")
    private String season;

    private MultipartFile img_file;

}
