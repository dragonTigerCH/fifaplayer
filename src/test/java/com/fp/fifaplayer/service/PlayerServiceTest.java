package com.fp.fifaplayer.service;

import com.fp.fifaplayer.domain.Player;
import com.fp.fifaplayer.form.PlayerForm;
import com.fp.fifaplayer.repository.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PlayerServiceTest {

    @Autowired
    PlayerService playerService;
    @Autowired
    PlayerRepository playerRepository;


   @Test
   @DisplayName("플레이어 저장")
   public void playerSave() throws Exception {
       //given
       PlayerForm playerForm = new PlayerForm("박지성",173,73,"보통","1999.08.17","대한민국","MF");
       Long savedPlayer = playerService.save(playerForm);
       //when
       Player player = playerRepository.findById(savedPlayer).orElse(null);
       //then
       assertEquals(playerForm.getName(),player.getName(),"플레이가 저장 되지 않았습니다.");
   }
}