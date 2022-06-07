package com.fp.fifaplayer.controller;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.domain.Rating;
import com.fp.fifaplayer.form.RatingSaveForm;
import com.fp.fifaplayer.repository.RatingRepository;
import com.fp.fifaplayer.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rating")
public class RatingController {

    private final RatingService ratingService;


    @PostMapping
    @ResponseBody
    public String rating(RatingSaveForm ratingSaveForm, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();
        String result = ratingService.ratingSave(ratingSaveForm, member);

        return result;
    }
}
