package io.keede.bootlateststarter.security.v2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.keede.bootlateststarter.security.v2.config.jwt.JwtTokenProvider;
import io.keede.bootlateststarter.security.v2.filter.BootAuthenticationSuccessHandlerV2;
import io.keede.bootlateststarter.security.v2.filter.LoginAuthenticationFilter;
import io.keede.bootlateststarter.security.v1.handler.BootAuthenticationEntryPoint;
import io.keede.bootlateststarter.security.v1.handler.BootAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


/**
 * @author kyh
 * Created on 2024/01/24
 */
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    private static final String LOGIN_API_URI = "/api/login";
    private static final String LOGOUT_API_URI = "/api/logout";

    public SecurityConfig(
            final UserDetailsService userDetailsService,
            final JwtTokenProvider jwtTokenProvider,
            final ObjectMapper objectMapper
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);

        sharedObject.userDetailsService(this.userDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        http.authenticationManager(authenticationManager);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers(
                                        antMatcher("/h2-console/**")
                                ).permitAll()
                                .requestMatchers(
                                        antMatcher("/admin/**")
                                ).hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                .addFilterAt(
                        this.abstractAuthenticationProcessingFilter(
                                authenticationManager,
                                authenticationSuccessHandler()
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(exceptionConfigurer ->
                        exceptionConfigurer.authenticationEntryPoint(
                                this.authenticationEntryPoint()
                        )
                )
                .headers(
                        headersConfigurer ->
                                headersConfigurer
                                        .frameOptions(
                                                HeadersConfigurer.FrameOptionsConfig::sameOrigin
                                        )
                );

        return http.build();
    }

    public AbstractAuthenticationProcessingFilter abstractAuthenticationProcessingFilter(
            final AuthenticationManager authenticationManager,
            final AuthenticationSuccessHandler authenticationSuccessHandler
    ) {
        return new LoginAuthenticationFilter(
                LOGIN_API_URI,
                authenticationManager,
                authenticationSuccessHandler
        );
    }

    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new BootAuthenticationSuccessHandlerV2(
                this.jwtTokenProvider,
                this.objectMapper
        );
    }

    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new BootAuthenticationEntryPoint();
    }

}
