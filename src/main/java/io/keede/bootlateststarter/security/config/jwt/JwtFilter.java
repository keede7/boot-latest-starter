package io.keede.bootlateststarter.security.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


/**
 * @author kyh
 * Created on 2024/01/23
 */
public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    ) throws IOException, ServletException {

        System.out.println("Execute JWT Filter");

        /*
            1. 헤더의 값을 찾는다.
           2. 헤더에서 Authorization 의 값을 가져온다.
           3. Authorization 값(token)을 검증한다.
           4. 예외가 발생하지않으면 요청을 진행한다.
         */

        chain.doFilter(request, response);

    }
}
