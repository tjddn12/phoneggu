package com.jsbs.casemall.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 인증 인가 부여
    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
                // 요청에 따른 인가 설정
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.requestMatchers("/member/**").authenticated(); // 인증된 회원만 member/밑에 모든  경로에 접근 가능 예를 들면 마이페이지 구매 진행 이런거
                    authorizeRequests.requestMatchers("/shop/**") // shop /  하위 요청은 seller 판매자만 들어갈수 있게 접근 제어
                            .hasRole("seller");

                    authorizeRequests.requestMatchers("/admin/**") // 관리자는 관리자 권한이 있는 사람만 접근 하게
                            .hasRole("ADMIN");

                    authorizeRequests.anyRequest().permitAll(); // 그 외에 는 접근 허용
                })
                .formLogin(login->login
                        .usernameParameter("userId")
                        .passwordParameter("userPw")
                        .loginPage("/login")
                        .defaultSuccessUrl("/"))
                ;


        return http.build();
    }




    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
