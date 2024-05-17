package com.jsbs.casemall.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 인증 인가 부여
    @Bean
    public SecurityFilterChain basicSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(atz->atz
                    .requestMatchers("/login").permitAll() // 로그인 페이지는 인증 없이 접근 가능하도록 설정
                .anyRequest().authenticated())

                .formLogin((login) -> login
                        .usernameParameter("userId")
                        .failureUrl("/member/login/error")
                        .loginPage("/member/login") // 로그인 페이지로
                        .defaultSuccessUrl("/"))
                .logout(logout->logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/"))


        ;


        return http.build();
    }




    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
