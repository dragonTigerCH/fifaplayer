package com.fp.fifaplayer.adapter;

import com.fp.fifaplayer.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.List;

@Getter
public class MemberAdapter extends User implements Serializable {

    private Member member;

    public MemberAdapter(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority(member.getAuth())));
        this.member = member;
    }

}
