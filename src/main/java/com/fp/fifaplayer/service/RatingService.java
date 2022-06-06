package com.fp.fifaplayer.service;


import com.fp.fifaplayer.domain.Datan;
import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.domain.Rating;
import com.fp.fifaplayer.form.RatingSaveForm;
import com.fp.fifaplayer.repository.DatanRepository;
import com.fp.fifaplayer.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final DatanRepository datanRepository;

    public String ratingSave(RatingSaveForm ratingSaveForm, Member member) {

        Long datan_id = ratingSaveForm.getDatan_id();
        Long member_Id = member.getId();

        //평점 중복 체크
        Rating duplicationResult = ratingRepository.findByDatan_idAndMember_id(datan_id, member_Id);

        if (duplicationResult != null){
            return "duplication";
        }


        Datan datan = datanRepository.findById(datan_id).orElseThrow(() -> new IllegalArgumentException("선수 정보를 찾을 수 없습니다."));
        ratingRepository.save(Rating.builder()
                        .datan(datan)
                        .member(member)
                        .score(ratingSaveForm.getScore())
                .build());
        return "ok";
    }
    //평점 평균내는 메소드
    public double scoreAverage(List<Rating> ratingList) {
        double score = 0;
        double value = 0;
        double result = 0;
        for (Rating rating : ratingList) {
            score += rating.getScore();
        }
        //평균값 나누기
        if (ratingList.size() != 0){
            value = score / ratingList.size();
        }
            //소수점 변환
            String format = String.format("%.1f", value);
            //문자열 정수변환
            result = Double.parseDouble(format);
        return result;
    }
}
