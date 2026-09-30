package com.validator.web.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.validator.web.validator.interceptor.ValidatorInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    ValidatorInterceptor validatorInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        List<String> excludePathPatterns = Arrays.asList(
            "/", // 홈 페이지
            "/static/**", // 정적 리소스
            "/error", // 에러 페이지
            "/favicon.ico", // 파비콘
            "/actuator/**", // 액추에이터 엔드포인트
            "/js/**", // JavaScript 파일
            "/css/**", // CSS 파일
            "/type1/**", // Type1 폰트
            "/.well-known/**" // well-known 경로
        );

        registry.addInterceptor(validatorInterceptor)
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns(excludePathPatterns); // 정적 리소스와 에러 페이지는 제외
    }   

}
