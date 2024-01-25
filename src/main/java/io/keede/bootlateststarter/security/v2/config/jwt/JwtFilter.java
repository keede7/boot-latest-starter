package io.keede.bootlateststarter.security.v2.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


/**
 * @author kyh
 * Created on 2024/01/23
 */
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final String loginUrl;
    private final JwtTokenProvider tokenProvider;
    private final SecurityContext securityContext;
    private final SecurityContextRepository securityContextRepository;

    public JwtFilter(
            final String loginUrl,
            final JwtTokenProvider tokenProvider,
            final SecurityContext securityContext,
            final SecurityContextRepository securityContextRepository
    ) {
        this.loginUrl = loginUrl;
        this.tokenProvider = tokenProvider;
        this.securityContext = securityContext;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        // 매번 Security의 필터에 의해서 ContextHolder에 담겨있는 정보가 요청이 끝난 후에 지워진다.
        if(!requestURI.equals(this.loginUrl)) {
            System.out.println("로그인 요청을 하지 않았다.");
            logger.debug("SecurityContextHolder.getContext().getAuthentication() : {}", SecurityContextHolder.getContext().getAuthentication());
            String jwt = resolveToken(request);
            System.out.println("jwt = " + jwt);
            System.out.println("this.tokenProvider.bindAuthorizationToken(jwt) = " + this.tokenProvider.bindAuthorizationToken(jwt));

            if (StringUtils.hasText(jwt)) {

                this.securityContext.setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                            "name",
                            "pass",
                            Collections.singletonList(
                                    new SimpleGrantedAuthority("ROLE_MEMBER")
                            )
                        )
                );
                this.securityContextRepository.saveContext(this.securityContext, request, response);

            } else {
                logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
