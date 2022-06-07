package com.fp.fifaplayer.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer height;
    private Integer weight;
    private String bodyform;
    private String birthday;
    private String country;
    private String position;


    @Builder
    public Player(Long id, String name, Integer height, Integer weight, String bodyform, String birthday, String country, String position) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.bodyform = bodyform;
        this.birthday = birthday;
        this.country = country;
        this.position = position;
    }
}
