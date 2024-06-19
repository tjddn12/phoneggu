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
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/user/**", "/order/**","/pay/**","/cart/**").authenticated() // 로그인성공한 사람만  user /밑에 모든  경로에 접근 가능 예를 들면 마이페이지 구매 진행 이런거
                        .requestMatchers("/admin/**") // 관리자로 저장된 회원만 admin / 아래 모든 모든곳에 접근 가능
                        .hasRole("ADMIN")
                        .anyRequest().permitAll() // 그 외에 는 접근 허용
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")  // 로그인 페이지 설정
                                .defaultSuccessUrl("/userEdit", true) // 성공 시 이동할 페이지 설정
                )
                .formLogin(login -> login
                        .usernameParameter("userId")
                        .passwordParameter("userPw")
                        .loginPage("/login") // 로그인 페이지 설정
                        .defaultSuccessUrl("/") // 성공 시 이동할 페이지 설정
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 메인페이지로 갈 수 있게
                        .invalidateHttpSession(true)) // 세션에 정보 삭제
//                .sessionManagement(session -> session
//                        .invalidSessionUrl("/login?invalid-session=true"))
        ;


        return http.build();
    }


    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
