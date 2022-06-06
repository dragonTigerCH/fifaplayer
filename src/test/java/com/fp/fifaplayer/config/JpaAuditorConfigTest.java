package com.fp.fifaplayer.config;

import com.fp.fifaplayer.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class JpaAuditorConfigTest {

    @Autowired
    EntityManager em;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void BaseEntityTest() throws Exception {
        //given

        //when

        //then
    }

}