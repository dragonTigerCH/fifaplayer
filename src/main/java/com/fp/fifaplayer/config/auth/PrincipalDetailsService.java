package com.fp.fifaplayer.config.auth;

import com.fp.fifaplayer.adapter.MemberAdapter;
import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//시큐리티 설정에서 loginProcessingUrl("/member/login");
// /member/login 요청이 오면 자동으로 UserDetailsService 타입으로 Ioc 되어있는 loadUserByUsername 메서드가 실행
@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        log.info("일반 로그인");

        return new PrincipalDetails(member);


    }
}
