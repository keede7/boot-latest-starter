package io.keede.bootlateststarter.security.v1.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 로그아웃 성공 이후 처리에 대한 핸들러
 *
 * @author keede
 * Created on 2023/09/30
 */
public class BootLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException {

        System.out.println("로그아웃 성공");

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println("로그아웃 성공!");

    }
}
