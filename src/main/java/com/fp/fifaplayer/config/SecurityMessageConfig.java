package com.fp.fifaplayer.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class SecurityMessageConfig {

    @Bean
    public MessageSource messageSource() {
        Locale.setDefault(Locale.KOREA); //위치 한국
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setDefaultEncoding("UTF-8"); //인코딩 설정
        messageSource.setBasenames("classpath:message/security_message", "classpath:org/springframework/security/messages"); // 커스텀한 properties 파일, security properties 파일 순서대로 설정
        return messageSource;
    }

}
