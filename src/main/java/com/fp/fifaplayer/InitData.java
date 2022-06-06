package com.fp.fifaplayer;

import com.fp.fifaplayer.domain.Datan;
import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.domain.Player;
import com.fp.fifaplayer.domain.Season;
import com.fp.fifaplayer.embeddable.Ability;
import com.fp.fifaplayer.repository.DatanRepository;
import com.fp.fifaplayer.repository.MemberRepository;
import com.fp.fifaplayer.repository.PlayerRepository;
import com.fp.fifaplayer.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
@RequiredArgsConstructor
public class InitData {

    private final MemberRepository memberRepository;
    private final DatanRepository datanRepository;
    private final SeasonRepository seasonRepository;
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;



    @PostConstruct
    public void initDatans(){


        Player parkJiSung = new Player(null, "박지성", 178, 73, "보통", "1981.02.25", "대한민국", "MF");
        Player son = new Player(null, "손흥민", 183, 78, "보통", "1992.07.08", "대한민국", "FW");
        Player ronaldo = new Player(null, "크리스티아누 호날두", 187, 83, "보통", "1985.02.05", "포르투갈", "FW");
        Player drogba = new Player(null, "디디에 드로그바", 189, 91, "건장", "1978.03.11", "코트디부아르", "FW");
        Player neymar = new Player(null, "네이마르 주니오르", 175, 68, "마름", "1992.02.05", "브라질", "FW");
        Season icon = new Season(null, "ICON", "ICON.png");
        Season loyalHeroes = new Season(null, "Loyal Heroes", "Loyal Heroes.png");
        Season captain = new Season(null, "Captain", "Captain.png");

        playerRepository.save(parkJiSung);
        playerRepository.save(ronaldo);
        playerRepository.save(drogba);
        playerRepository.save(neymar);
        playerRepository.save(son);

        seasonRepository.save(icon);
        seasonRepository.save(captain);
        seasonRepository.save(loyalHeroes);

        Ability iconParkJiSungAbility = new Ability(24, 110, 102, 106, 109, 106, 105, 90);
        Datan iconParkJiSung = new Datan(null, iconParkJiSungAbility, iconParkJiSungAbility.overall(), "맨체스터 유나이티드", "ICON 박지성.png", icon, parkJiSung);

        Ability loyalHeroesParkJiSungAbility = new Ability(20, 98, 89, 93, 99, 89, 89, 85);
        Datan loyalHeroesParkJiSung = new Datan(null, loyalHeroesParkJiSungAbility, loyalHeroesParkJiSungAbility.overall(), "맨체스터 유나이티드", "Loyal Heroes 박지성.png", loyalHeroes, parkJiSung);

        Ability captainParkJiSungAbility = new Ability(23, 106, 93, 100, 105, 98, 98, 89);
        Datan captainParkJiSung = new Datan(null, captainParkJiSungAbility, captainParkJiSungAbility.overall(), "맨체스터 유나이티드", "Captain 박지성.png", captain, parkJiSung);

        Ability captainSonAbility = new Ability(23, 110, 105, 94, 102, 54, 91, 80);
        Datan captainSon = new Datan(null, captainSonAbility, captainSonAbility.overall(), "토트넘 훗스퍼", "Captain 손흥민.png", captain, son);

        Ability loyalHeroesSonAbility = new Ability(17, 100, 92, 84, 93, 48, 88, 79);
        Datan loyalHeroesSon = new Datan(null, loyalHeroesSonAbility, loyalHeroesSonAbility.overall(), "토트넘 훗스퍼", "Loyal Heroes 손흥민.png", loyalHeroes, son);

        Ability loyalHeroesDrogbaAbility = new Ability(21, 94, 102, 81, 92, 48, 102, 78);
        Datan loyalHeroesDrogba = new Datan(null, loyalHeroesDrogbaAbility, loyalHeroesDrogbaAbility.overall(), "첼시", "Loyal Heroes 디디에 드로그바.png", loyalHeroes, drogba);

        Ability iconDrogbaAbility = new Ability(26, 107, 113, 93, 105, 64, 114, 88);
        Datan iconDrogba = new Datan(null, iconDrogbaAbility, iconDrogbaAbility.overall(), "첼시", "ICON 디디에 드로그바.png", icon, drogba);

        Ability captainDrogbaAbility = new Ability(24, 101, 108, 86, 99, 48, 107, 85);
        Datan captainDrogba = new Datan(null, captainDrogbaAbility, captainDrogbaAbility.overall(), "첼시", "Captain 디디에 드로그바.png", captain, drogba);

        Ability captainNeymarAbility = new Ability(24, 109, 103, 99, 109, 45, 80, 79);
        Datan captainNeymar = new Datan(null, captainNeymarAbility, captainNeymarAbility.overall(), "파리 생제르맹", "Captain 네이마르 주니오르.png", captain, neymar);

        Ability captainRonaldoAbility = new Ability(25, 105, 109, 87, 104, 43, 93, 78);
        Datan captainRonaldo = new Datan(null, captainRonaldoAbility, captainRonaldoAbility.overall(), "맨체스터 유나이티드", "Captain 크리스티아누 호날두.png", captain, ronaldo);

        Ability loyalHeroesRonaldoAbility = new Ability(23, 105, 105, 86, 97, 40, 94, 71);
        Datan loyalHeroesRonaldo = new Datan(null, loyalHeroesRonaldoAbility, loyalHeroesRonaldoAbility.overall(), "유벤투스", "Loyal Heroes 크리스티아누 호날두.png", loyalHeroes, ronaldo);

        datanRepository.save(iconParkJiSung);
        datanRepository.save(loyalHeroesParkJiSung);
        datanRepository.save(captainParkJiSung);
        datanRepository.save(captainSon);
        datanRepository.save(loyalHeroesSon);
        datanRepository.save(loyalHeroesDrogba);
        datanRepository.save(iconDrogba);
        datanRepository.save(captainDrogba);
        datanRepository.save(captainNeymar);
        datanRepository.save(captainRonaldo);
        datanRepository.save(loyalHeroesRonaldo);


    }

    @PostConstruct
    public void initMembers(){
        Member member1 = new Member(null,"kyu26001@naver.com", passwordEncoder.encode("chlalsrb"),"클래식",null,null,"ROLE_USER");
        Member member2 = new Member(null,"yoho9907@gmail.com", passwordEncoder.encode("chlalsrb"),"일로그",null,null, "ROLE_ADMIN");
        memberRepository.save(member1);
        memberRepository.save(member2);




    }




}
