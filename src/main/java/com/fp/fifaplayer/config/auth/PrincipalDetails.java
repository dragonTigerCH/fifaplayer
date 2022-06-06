package com.fp.fifaplayer.config.auth;

import com.fp.fifaplayer.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인을 진행이 완료가되면 시큐리티 session을 만들어준다.(Security ContextHolder)
//오브젝트 타입 => Authentication 타입 객체
//Authentication 안에 User 정보가 있어야됨
//User(Member)오브젝트타입 => UserDetails 타입 객체

// Security Session => Authenticaiton => UserDetails

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member;
    private Map<String,Object> attributes;


    //일반 로그인
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    //OAuth 로그인
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    //권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : member.getAuth().split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        // ex) 1년동안 회원이 로그인을 안할시 휴먼 계정 처리
        // 현재시간 - 로그인시간 = 1년을 초과하면 false처리
        return true; // true -> 사용 가능
    }

    //OAuth2
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
