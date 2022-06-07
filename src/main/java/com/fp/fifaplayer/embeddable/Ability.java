package com.fp.fifaplayer.embeddable;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ability {

    private Integer salaryup;
    private Integer speed;
    private Integer shooting;
    private Integer pass;
    private Integer dribble;
    private Integer defence;
    private Integer physical;
    private Integer blocking;


    public Integer overall() {
        return (getSpeed() + getShooting() + getPass() + getDribble()
                + getDefence() + getPhysical() + getBlocking()) / 7;
    }


}
