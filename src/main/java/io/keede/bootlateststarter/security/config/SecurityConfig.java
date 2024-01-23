package io.keede.bootlateststarter.security.config;

import io.keede.bootlateststarter.security.filter.LoginAuthenticationFilter;
import io.keede.bootlateststarter.security.handler.BootAuthenticationEntryPoint;
import io.keede.bootlateststarter.security.handler.BootAuthenticationSuccessHandler;
import io.keede.bootlateststarter.security.handler.BootLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Security 전체 설정부
 * @author keede
 * Created on 2023/08/15
 */
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private static final String LOGIN_API_URI = "/api/login";
    private static final String LOGOUT_API_URI = "/api/logout";

    public SecurityConfig(
            final UserDetailsService userDetailsService
    ) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);

        sharedObject.userDetailsService(this.userDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        http.authenticationManager(authenticationManager);

        http
                .csrf(AbstractHttpConfigurer::disable)
//            .formLogin(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequest ->
                    /*
                        h2 자동 설정이 추가적으로 들어갈 경우, AbstractRequestMatcherRegistry 의 requestMatcher가 동작한다.
                        여기서 h2관련 서블릿? 설정이 추가되는데 이부분에서 예외가 발생한다.
                        자세한건 더 알아봐야한다.
                        AntPathRequestMatcher.antMatcher 를 사용하면 위의 문제는 쉽게 해결이된다.
                        하지만 requestMatcher("경로") 를 쓰게 되면 위에 언급한 문제가 발생한다.
                        만약 다른 설정들도 추가로 존재하게 됐을 때 이 문제가 발생하는지 확인이 된다면
                        AntPathRequestMatcher.antMatcher를 쓰는 방향이 조금 더 개발에 도움이 될까 싶다.
                     */
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
                        this.abstractAuthenticationProcessingFilter(
                                authenticationManager,
                                authenticationSuccessHandler()
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                // 로그아웃 설정
                .logout(logoutConfig ->
                        logoutConfig
                                .logoutUrl(LOGOUT_API_URI)
                                .logoutSuccessHandler(
                                        this.logoutSuccessHandler()
                                )
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

    /*
        This is not recommended -- please use permitAll via HttpSecurity#authorizeHttpRequests instead.
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // 정적 리소스 spring security 대상에서 제외
//        return (web) -> web
//                .ignoring()
//                .requestMatchers(
//                        PathRequest.toStaticResources()
//                                .atCommonLocations()
//                );
//    }

    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new BootAuthenticationSuccessHandler();
    }

    public LogoutSuccessHandler logoutSuccessHandler() {
        return new BootLogoutSuccessHandler();
    }

    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new BootAuthenticationEntryPoint();
    }

}
