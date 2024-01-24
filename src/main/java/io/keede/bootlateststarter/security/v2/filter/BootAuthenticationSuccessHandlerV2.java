package io.keede.bootlateststarter.security.v2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.keede.bootlateststarter.domains.user.dto.AuthenticationDetail;
import io.keede.bootlateststarter.security.v1.dto.LoginRequestDto;
import io.keede.bootlateststarter.security.v2.config.jwt.JwtTokenProvider;
import io.keede.bootlateststarter.security.v2.config.jwt.Token;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 로그인 성공 이후 처리에 대한 핸들러 역할
 * @author keede
 * Created on 2023/09/10
 */
public class BootAuthenticationSuccessHandlerV2 implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public BootAuthenticationSuccessHandlerV2(
            final JwtTokenProvider jwtTokenProvider,
            final ObjectMapper objectMapper
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    // 로그인을 성공하면 JWT토큰을 발급한다.
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        AuthenticationDetail principal = (AuthenticationDetail) authentication.getPrincipal();

        Token jwtToken = jwtTokenProvider.createJwtToken(principal);

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().println(this.objectMapper.writeValueAsString(jwtToken));
    }

}
