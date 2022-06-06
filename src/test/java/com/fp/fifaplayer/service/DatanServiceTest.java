package com.fp.fifaplayer.service;

import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.domain.Player;
import com.fp.fifaplayer.domain.Season;
import com.fp.fifaplayer.form.DatanForm;
import com.fp.fifaplayer.repository.PlayerRepository;
import com.fp.fifaplayer.repository.SeasonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class DatanServiceTest {

    @Autowired
    DatanService datanService;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    SeasonRepository seasonRepository;

    Member member = null;
    Player mrHong = null;
    Season lol = null;


    @BeforeEach
    void sample() throws IOException {
        mrHong = new Player(null,"홍동우",183,69,"마름","1994.01.05","한국","DF");
        lol = new Season(null,"LOL",null);

    }


    @Test
    public void 플레이어_시즌_입력_테스트() throws Exception {
        //given - 무언가 주어지고
        MockMultipartFile file = new MockMultipartFile("img_file",
                "test.png",
                "image/png",
                new FileInputStream("C:\\fifaPrj\\fifaplayer\\src\\main\\resources\\static\\img\\배경.png"));
        DatanForm datanForm = new DatanForm(4,90,90,90,90,90,90,90,"토트넘","홍동우","LOL",file);

        playerRepository.save(mrHong);
        seasonRepository.save(lol);
        //when - 무언가 실행하면
        datanService.save(datanForm);
        //then - 결과
        assertThat(datanForm.getDefence()).isEqualTo(90);
    }
}