package io.keede.bootlateststarter;


import io.keede.bootlateststarter.security.v2.config.jwt.JwtTokenProvider;
import io.keede.bootlateststarter.security.v2.config.jwt.Token;
import io.keede.bootlateststarter.security.v2.dto.LoginRequestDtoV2;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
* @author keede
* Created on 2023/08/23
*/
@Controller
public class SampleController {

    private final JwtTokenProvider jwtTokenProvider;

    private final SecurityContext securityContext;
    private final SecurityContextRepository securityContextRepository;

    public SampleController(
            final JwtTokenProvider jwtTokenProvider,
            final SecurityContext securityContext,
            final SecurityContextRepository securityContextRepository
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityContext = securityContext;
        this.securityContextRepository = securityContextRepository;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("ss : " +request.getSession().getAttribute("SPRING_SECURITY_CONTEXT"));
        return "index";
    }

    @GetMapping("/auth/test")
    @ResponseBody
    public String get() {
        System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());
        return "로그인 완료";
    }

    // NOTE : Presentation Layer에서 로그인을 구현할 경우 사용한다.
//    @PostMapping("/api/login")
//    public ResponseEntity<Token> login(
//            @RequestBody LoginRequestDtoV2 loginRequestDtoV2,
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) {
//        Token jwtToken = this.jwtTokenProvider.createJwtToken(loginRequestDtoV2);
//
//        this.securityContext.setAuthentication(loginRequestDtoV2.toAuthenticationToken());
//        SecurityContextHolder.setContext(this.securityContext);
//        this.securityContextRepository.saveContext(this.securityContext, request, response);
//
//        return ResponseEntity.ok(jwtToken);
//    }

}
