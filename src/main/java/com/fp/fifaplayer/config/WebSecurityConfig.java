package com.fp.fifaplayer.config;


import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.config.auth.PrincipalDetailsService;
import com.fp.fifaplayer.config.oauth.PrincipalOauth2UserService;
import com.fp.fifaplayer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
//@EnableGlobalMethodSecurity(securedEnabled = true) = @Secured 어노테이션 활성화
//(prePostEnabled = true) = PreAuthorize,PostAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final PrincipalDetailsService principalDetailsService;
    private final PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/member/register","/member/auth").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/board/**","/member/**","/dataninfo/**").hasAnyRole("USER","ADMIN")
                    .antMatchers("/**").permitAll()
                    .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login").permitAll() // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행(POST)
                .defaultSuccessUrl("/")

                .and()

                .logout()
                .logoutSuccessUrl("/login") // 로그아웃 성공시 리다이렉트 주소
                .invalidateHttpSession(true)// 세션 날리기

                .and()
                // oauth2Login 설정 시작
                .oauth2Login()
                .loginPage("/login") //로그인이 완료된후 후처리가 필요함. 1.코드받기(인증) 2.엑세스토큰(사용자정보에 대한 권한) 3.사용자 프로필정보를 가져와서
                // oauth2Login 성공 이후의 설정을 시작
                .userInfoEndpoint()//4-1. 가져온 정보를 토대로 회원가입을 진행시키기도함 4-2. 정보(이름,집주소)가 더 필요하면 추가정보를 입력하게한다.
                // customOAuth2UserService에서 처리하겠다.
                .userService(principalOauth2UserService); //구글로그인 완료후 코드가아니라 엑세스토큰 +사용자 프로필정보를 받는다. 누가? oauth2 라이브러리가 받아준다.


        //중복 로그인
        http.sessionManagement()
                .maximumSessions(1) //세션 최대 허용 수
                .maxSessionsPreventsLogin(false); // false이면 중복 로그인하면 이전 로그인이 풀린다.

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception { // 9
        auth.userDetailsService(principalDetailsService)
                // 해당 서비스(userService)에서는 UserDetailsService를 implements해서
                // loadUserByUsername() 구현해야함 (서비스 참고)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }






}