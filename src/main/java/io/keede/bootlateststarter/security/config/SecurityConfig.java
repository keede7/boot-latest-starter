package io.keede.bootlateststarter.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
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
            .headers(
                    headersConfigurer ->
                            headersConfigurer
                                    .frameOptions(
                                            HeadersConfigurer.FrameOptionsConfig::sameOrigin
                                    )
                                    .contentSecurityPolicy( policyConfig ->
                                            policyConfig.policyDirectives(
                                            "script-src 'self'; " + "img-src 'self'; " +
                                                    "font-src 'self' data:; " + "default-src 'self'; " +
                                                    "frame-src 'self'"
                                            )
                                    )
            );

        return http.build();
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
