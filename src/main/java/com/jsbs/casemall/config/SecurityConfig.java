package com.jsbs.casemall.config;

import com.jsbs.casemall.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    // 인증 인가 부여
    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {

        http

                .csrf(AbstractHttpConfigurer::disable)
                // 요청에 따른 인가 설정
//                .authorizeHttpRequests((request) -> request
//                        .requestMatchers("/user/**", "/order/**","/pay/**").authenticated() // 로그인성공한 사람만  user /밑에 모든  경로에 접근 가능 예를 들면 마이페이지 구매 진행 이런거
//                        .requestMatchers("/admin/**") // 관리자로 저장된 회원만 admin / 아래 모든 모든곳에 접근 가능
//                        .hasRole("ADMIN")
//                        .requestMatchers("/test/**").authenticated()
//                        .anyRequest().permitAll() // 그 외에 는 접근 허용
//                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/login").permitAll()
//                                .anyRequest().authenticated()
                                .requestMatchers("/user/**", "/order/**","/pay/**").authenticated() // 로그인성공한 사람만  user /밑에 모든  경로에 접근 가능 예를 들면 마이페이지 구매 진행 이런거
                                .requestMatchers("/admin/**") // 관리자로 저장된 회원만 admin / 아래 모든 모든곳에 접근 가능
                                .hasRole("ADMIN")
                                .requestMatchers("/test/**").authenticated()
                                .anyRequest().permitAll() // 그 외에 는 접근 허용
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .defaultSuccessUrl("/home", true)
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .oidcUserService(oidcUserService())
                                )
                )
                .formLogin(login -> login
                        .usernameParameter("userId")
                        .passwordParameter("userPw")
                        .loginPage("/login")
                        .defaultSuccessUrl("/"))

                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 성공시 메인페이지로 갈수있게
                        .invalidateHttpSession(true)) // 세션에 정보 삭제
//                .sessionManagement(session -> session
//                        .invalidSessionUrl("/login?invalid-session=true"))
        ;


        return http.build();
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}