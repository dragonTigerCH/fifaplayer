package com.fp.fifaplayer.service;

import com.fp.fifaplayer.domain.*;
import com.fp.fifaplayer.embeddable.Ability;
import com.fp.fifaplayer.form.RatingSaveForm;
import com.fp.fifaplayer.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RatingServiceTest {

    @Autowired
    RatingService ratingService;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    DatanRepository datanRepository;

    @Test
    @DisplayName("평점 저장")
    public void ratingSave() throws Exception {
        //given
        Ability ability = new Ability(20, 100, 100, 100, 100, 100, 100, 100);

        Season season = new Season(null, "테스트시즌", "테스트파일");
        Player player = new Player(null, "테스트플레이어", 180, 80, "건장", "1993.08.17", "한국", "FW");
        Datan datan = new Datan(null, ability, ability.overall(), "테스트클럽", "테스트파일", season, player);
        Member member = new Member(null,"yoho98@daum.net","chlalsrb","테스트1",null,null,"ROLE_USER");
        memberRepository.save(member);
        playerRepository.save(player);
        seasonRepository.save(season);
        datanRepository.save(datan);
        RatingSaveForm ratingSaveForm = new RatingSaveForm(datan.getId(), 5);

        //when

        //첫 입력 ok
        String result1 = ratingService.ratingSave(ratingSaveForm, member);
        //중복입력 duplication
        String result2 = ratingService.ratingSave(ratingSaveForm, member);

        Rating resultRating = ratingRepository.findByDatan_idAndMember_id(datan.getId(), member.getId());
        //then
        assertEquals(result1,"ok");
        assertEquals(result2,"duplication");

        assertEquals(resultRating.getScore(),ratingSaveForm.getScore());

    }


}