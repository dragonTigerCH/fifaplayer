package com.fp.fifaplayer.repository;

import com.fp.fifaplayer.domain.Board;
import com.fp.fifaplayer.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//CRUD 함수를 JpaRepository가 들고 있음.
//@Repository라는 어노테이션이 없어도 Ioc가 된다. 이유는 JpaRepository를 상속했기때문에
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Member findMemberByEmailAndProviderId(String email, String providerId);

    Optional<Member> findByNickname(String nickname);


}
