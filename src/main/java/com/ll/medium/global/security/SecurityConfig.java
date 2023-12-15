package com.ll.medium.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                                authorizeRequests
                                .requestMatchers("/gen/**")
                                .permitAll()
                                .requestMatchers("/resource/**")
                                .permitAll()
                                .requestMatchers("/h2-console/**")
                                .permitAll()
                                .requestMatchers("/adm/**")
                                .hasRole("ADMIN")
                                .anyRequest()
                                .permitAll()
//                                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//                                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                )
                .headers(headers ->
                        headers.frameOptions(frameOptions ->
                                frameOptions.sameOrigin()))
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers("/h2-console/**"))
                .formLogin((formLogin) ->
                        formLogin.loginPage("/member/login")
                                .defaultSuccessUrl("/?msg=" + URLEncoder.encode("환영합니다.", StandardCharsets.UTF_8))
                                .failureUrl("/member/login?failMsg=" + URLEncoder.encode("fail", StandardCharsets.UTF_8)))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}