package com.fp.fifaplayer.service;

import com.fp.fifaplayer.domain.Datan;
import com.fp.fifaplayer.domain.Player;
import com.fp.fifaplayer.domain.Season;
import com.fp.fifaplayer.embeddable.Ability;
import com.fp.fifaplayer.file.FileStore;
import com.fp.fifaplayer.file.UploadFile;
import com.fp.fifaplayer.form.DatanForm;
import com.fp.fifaplayer.repository.DatanRepository;
import com.fp.fifaplayer.repository.PlayerRepository;
import com.fp.fifaplayer.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
@Transactional
public class DatanService {

    private final DatanRepository datanRepository;
    private final PlayerRepository playerRepository;
    private final SeasonRepository seasonRepository;
    private final FileStore fileStore;


    public Long save(DatanForm datanForm) throws IOException {

        Player player = playerRepository.findByName(datanForm.getPlayer()).orElseThrow(() -> new IllegalArgumentException("입력된 선수를 찾을 수 없습니다."));
        Season season = seasonRepository.findByName(datanForm.getSeason()).orElseThrow(() -> new IllegalArgumentException("입력된 시즌을 찾을 수 없습니다."));

        //중복 선수데이터체크
        Datan datan = datanRepository.findByPlayerIdAndSeasonId(player.getId(), season.getId());
        if (datan != null) {
            return 0l;
        }


        //파일 static에 저장하고 UUID로 변환시키기
        UploadFile img_file = fileStore.DatanStoreFile(datanForm.getImg_file());
        Ability ability = new Ability(datanForm.getSalaryup(), datanForm.getSpeed(), datanForm.getShooting(), datanForm.getPass()
                , datanForm.getDribble(), datanForm.getDefence(), datanForm.getPhysical(), datanForm.getBlocking());

        return datanRepository.save(Datan.builder()
                .ability(ability)
                .club(datanForm.getClub())
                .overall(ability.overall())
                .img_file(img_file.getStoreFileName())
                .player(player)
                .season(season)
                .build()).getId();
    }


}
