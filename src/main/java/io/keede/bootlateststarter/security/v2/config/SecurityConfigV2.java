package io.keede.bootlateststarter.security.v2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.keede.bootlateststarter.security.v2.config.jwt.JwtFilter;
import io.keede.bootlateststarter.security.v2.config.jwt.JwtTokenProvider;
import io.keede.bootlateststarter.security.v2.filter.LoginAuthenticationFilterV2;
import io.keede.bootlateststarter.security.v2.handler.BootAuthenticationEntryPointV2;
import io.keede.bootlateststarter.security.v2.handler.BootAuthenticationSuccessHandlerV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.*;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


/**
 * @author kyh
 * Created on 2024/01/24
 */
@Configuration
public class SecurityConfigV2 {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    private static final String LOGIN_API_URI = "/api/login";

    public SecurityConfigV2(
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
                                        antMatcher("/auth/**")
                                ).hasRole("MEMBER")
                                .requestMatchers(
                                        antMatcher("/h2-console/**")
                                ).permitAll()
                                .requestMatchers(
                                        antMatcher("/admin/**")
                                ).hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                .addFilterAt(
                        new JwtFilter(
                                LOGIN_API_URI,
                                this.jwtTokenProvider,
                                this.securityContext(),
                                this.securityContextRepository()
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAt(
                        this.securityContextHolderFilter(),
                        SecurityContextHolderFilter.class
                )
                // NOTE : 시큐리티 필터를 통한 로그인을 할 경우 사용.
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
        return new LoginAuthenticationFilterV2(
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
        return new BootAuthenticationEntryPointV2();
    }

    // NOTE : SecurityContext 와 관련되어 사용되는 객체들이 실제 필터가 동작하는 과정에서는 새 객체가 사용되어
    // SecurityContextHolder에 인증객체가 유지되지 않는다.
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new HttpSessionSecurityContextRepository(),
                new RequestAttributeSecurityContextRepository()
        );
    }

    @Bean
    public SecurityContext securityContext() {
        return new SecurityContextImpl();
    }

    // SecurityContextPersistenceFilter 가 사용되지 않게 되면서 내부 구현체가 다르게 사용됨을 확인.
    @Bean
    public SecurityContextHolderFilter securityContextHolderFilter() {
        return new SecurityContextHolderFilter(
                this.securityContextRepository()
        );
    }
}
