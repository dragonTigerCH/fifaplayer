package com.fp.fifaplayer.config.oauth;

import com.fp.fifaplayer.config.auth.PrincipalDetails;
import com.fp.fifaplayer.config.oauth.provider.FacebookUserInfo;
import com.fp.fifaplayer.config.oauth.provider.GoogleUserInfo;
import com.fp.fifaplayer.config.oauth.provider.NaverUserInfo;
import com.fp.fifaplayer.config.oauth.provider.OAuth2UserInfo;
import com.fp.fifaplayer.domain.Member;
import com.fp.fifaplayer.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {


    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    //구글로 부터 받은  userRequest 데이터에 대한 후처리되는 함수
    //메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("userRequest.getClientRegistration() = {}", userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인했는지 확인
        log.info("userRequest.getAccessToken().getTokenValue() = {}", userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Client라이브러리가 받아줌) -> AccessToken 요청
        //userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.

        log.info("super.loadUser(userRequest).getAttributes() = {}", oAuth2User.getAttributes());
        OAuth2UserInfo oAuth2UserInfo = null;
        String nickname = null;


        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            nickname = googleUserInfo.getName();
            log.info("구글 로그인 요청");
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
            FacebookUserInfo facebookUserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
            nickname = facebookUserInfo.getName();
            log.info("페이스북 로그인 요청");
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
            NaverUserInfo naverUserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
            nickname = naverUserInfo.getNickname();
            log.info("네이버 로그인 요청");
        } else {
            log.info("우리는 구글과 페이스북과 네이버만 지원합니다.");
        }


        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        /*String nickname = provider+"_"+providerId;*/
        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        String email = oAuth2UserInfo.getEmail();
        String auth = "ROLE_USER";

        Member member = memberRepository.findMemberByEmailAndProviderId(email, providerId);

        if (member != null) {
            log.info("가입되어있는 회원 입니다.");

        } else {
            log.info("OAuth2 로그인이 최초입니다.");


            member = Member.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .auth(auth)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            memberRepository.save(member);
        }

        log.info("소셜 로그인");
        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }


}
