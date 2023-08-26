package io.keede.bootlateststarter.security.config;

import io.keede.bootlateststarter.security.filter.LoginAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
* @author keede
* Created on 2023/08/15
*/
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);

        sharedObject.userDetailsService(this.userDetailsService);

        SessionAuthenticationStrategy sessionAuthenticationStrategy = http
                .getSharedObject(CompositeSessionAuthenticationStrategy.class);

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
                            ).hasRole("MEMBER")
                            .requestMatchers(
                                    antMatcher("/h2-console/**")
                            ).permitAll()
                            .anyRequest().permitAll()
            )
            .addFilterAt(
                    this.abstractAuthenticationProcessingFilter(authenticationManager, sessionAuthenticationStrategy),
                    UsernamePasswordAuthenticationFilter.class)
            .headers(
                    headersConfigurer ->
                            headersConfigurer
                                    .frameOptions(
                                            HeadersConfigurer.FrameOptionsConfig::sameOrigin
                                    )
            )
        ;

        return http.build();
    }

    public AbstractAuthenticationProcessingFilter abstractAuthenticationProcessingFilter(final AuthenticationManager authenticationManager,
                                                                                         final SessionAuthenticationStrategy sessionAuthenticationStrategy) {
        return new LoginAuthenticationFilter(
                "/api/login",
                authenticationManager,
                sessionAuthenticationStrategy
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
