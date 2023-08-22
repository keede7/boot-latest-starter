package io.keede.bootlateststarter.security.config;

import io.keede.bootlateststarter.security.filter.LoginAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
* @author keede
* Created on 2023/08/15
*/
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = sharedObject.build();

        http.authenticationManager(authenticationManager);

        http
            .csrf(AbstractHttpConfigurer::disable)
//            .formLogin(Customizer.withDefaults())
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequest ->
                    authorizeRequest
                            .requestMatchers(
                                    antMatcher("/auth/**")
                            ).authenticated()
                            .requestMatchers(
                                    antMatcher("/h2-console/**")
                            ).permitAll()
            )
            .addFilterAt(
                    this.abstractAuthenticationProcessingFilter(authenticationManager),
                    UsernamePasswordAuthenticationFilter.class)
            .headers(
                    headersConfigurer ->
                            headersConfigurer
                                    .frameOptions(
                                            HeadersConfigurer.FrameOptionsConfig::sameOrigin
                                    )
            );

        return http.build();
    }

    public AbstractAuthenticationProcessingFilter abstractAuthenticationProcessingFilter(final AuthenticationManager authenticationManager) {
        return new LoginAuthenticationFilter(
                "/api/login",
                authenticationManager
        );
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스 spring security 대상에서 제외
        return (web) ->
                web
                    .ignoring()
                    .requestMatchers(
                            PathRequest.toStaticResources().atCommonLocations()
                    );
    }

}
