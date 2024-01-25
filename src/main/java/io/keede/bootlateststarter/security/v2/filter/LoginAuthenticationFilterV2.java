package io.keede.bootlateststarter.security.v2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.keede.bootlateststarter.security.v2.dto.LoginRequestDtoV2;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

import java.io.IOException;

/**
 * 로그인 담당 필터
 * @author keede
 * Created on 2023/08/15
 *
 * 로그인 요청에 대해 가장 먼저 처리한다.
 */
public class LoginAuthenticationFilterV2 extends AbstractAuthenticationProcessingFilter {

    public LoginAuthenticationFilterV2(
            final String defaultFilterProcessesUrl,
            final AuthenticationManager authenticationManager,
            final AuthenticationSuccessHandler authenticationSuccessHandler
    ) {
        super(defaultFilterProcessesUrl, authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        // 로그인 이후 Context 생성 전략 설정
        setSecurityContextRepository(
                new DelegatingSecurityContextRepository(
                        new HttpSessionSecurityContextRepository(),
                        new RequestAttributeSecurityContextRepository()
                )
        );
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException, IOException {

        String method = request.getMethod();

        if (!method.equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        ServletInputStream inputStream = request.getInputStream();

        LoginRequestDtoV2 loginRequestDtoV2 = new ObjectMapper()
                .readValue(inputStream, LoginRequestDtoV2.class);

        return this.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDtoV2.username(),
                        loginRequestDtoV2.password()
                )
        );
    }

}
