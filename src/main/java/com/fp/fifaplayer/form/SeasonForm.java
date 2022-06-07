package com.fp.fifaplayer.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonForm {

    @NotBlank(message = "시즌 이름을 입력해주세요.")
    @Pattern(regexp = "^[0-9A-Za-z\\s-_\\.]{4,50}$", message = "영어 대소문자,공백,특수문자(.-_) 만 허용합니다.")
    private String name;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;

}
