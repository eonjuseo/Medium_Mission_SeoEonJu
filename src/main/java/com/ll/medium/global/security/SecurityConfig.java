package com.ll.medium.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
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
                        .defaultSuccessUrl("/")
                        .failureUrl("/member/login"))
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
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}