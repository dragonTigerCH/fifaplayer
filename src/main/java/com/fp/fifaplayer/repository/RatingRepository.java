package com.fp.fifaplayer.repository;

import com.fp.fifaplayer.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {


    Rating findByDatan_idAndMember_id(Long datan_id, Long member_id);


    List<Rating> findByDatan_id(Long datan_id);
}
