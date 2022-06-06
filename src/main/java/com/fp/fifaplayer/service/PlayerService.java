package com.fp.fifaplayer.service;

import com.fp.fifaplayer.domain.Player;
import com.fp.fifaplayer.form.PlayerForm;
import com.fp.fifaplayer.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;


    public Long save(PlayerForm playerForm) {
        return playerRepository.save(Player.builder()
                .name(playerForm.getName())
                .height(playerForm.getHeight())
                .weight(playerForm.getWeight())
                .bodyform(playerForm.getBodyform())
                .birthday(playerForm.getBirthday())
                .country(playerForm.getCountry())
                .position(playerForm.getPosition())
                .build()).getId();
    }


}
