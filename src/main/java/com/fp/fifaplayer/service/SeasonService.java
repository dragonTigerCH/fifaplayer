package com.fp.fifaplayer.service;

import com.fp.fifaplayer.domain.Season;
import com.fp.fifaplayer.form.SeasonForm;
import com.fp.fifaplayer.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;

    public Long save(SeasonForm seasonForm,String storeFileName) {
        return seasonRepository.save(Season.builder()
                .name(seasonForm.getName())
                .attachFile(storeFileName)
                .build()).getId();

    }
}
