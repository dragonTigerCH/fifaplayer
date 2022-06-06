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
public class PlayerForm {

    @NotBlank(message = "이름을 입력하세요.")
    @Pattern(regexp = "^[가-힣\\s]{1,20}[가-힣]{1,20}$",message = "알맞은 형식이아닙니다. * 한글과 공백만 허용합니다.")
    private String name;

    @NotNull(message = "신장(키)을 입력하세요.")
    @Range(min = 130,max = 250,message = "130cm이상 250cm이하만 입력가능합니다.")
    private Integer height;

    @NotNull(message = "몸무게를 입력하세요")
    @Range(min = 50,max = 200,message = "50kg이상 200kg이하만 입력가능합니다.")
    private Integer weight;

    @NotBlank(message = "체형를 입력하세요")
    @Pattern(regexp = "^(마름|보통|건장)$",
            message = "마름,보통,건장 만 입력 가능합니다.")
    private String bodyform;

    @NotBlank(message = "생년월일을 입력하세요.")
    @Pattern(regexp = "^(19[0-9][0-9]|20\\\\d{2})(\\.)(0[0-9]|1[0-2])(\\.)(0[1-9]|[1-2][0-9]|3[0-1])$",message = "19XX.XX.XX , 20XX.XX.XX 형식만 입력가능합니다")
    private String birthday;

    @NotBlank(message = "국적을 입력하세요.")
    @Pattern(regexp = "^[가-힣\\s]{2,44}$",message = "한글과 공백만 입력가능합니다.")
    private String country;

    @Pattern(regexp = "^(FW|MF|DF|GK)$",message = "FW / MF / DF / GK 중 한가지만 입력가능합니다.")
    @NotBlank(message = "포지션을 입력하세요.")
    private String position;



}
