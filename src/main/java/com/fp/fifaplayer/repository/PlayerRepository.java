package com.fp.fifaplayer.repository;

import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player,Long> {
    Optional<Player> findByName(String name);

}
