package io.keede.bootlateststarter.security.v2.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * @author kyh
 * Created on 2024/01/23
 */
public class JwtFilter extends OncePerRequestFilter {
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

        if(!requestURI.equals(this.loginUrl)) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String jwt = resolveToken(request);

            if (StringUtils.hasText(jwt) && authentication == null) {

                String issuer = this.tokenProvider.bindAuthorizationToken(jwt);

                this.securityContext.setAuthentication(
                        this.tokenProvider.toAuthentication(issuer)
                );
                this.securityContextRepository.saveContext(this.securityContext, request, response);

            } else {
                System.out.println("유효한 JWT 토큰이 없습니다, uri: " + requestURI);
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
