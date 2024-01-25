package io.keede.bootlateststarter.security.v2.dto;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

/**
 * @author kyh
 * Created on 2024/01/24
 */
public record LoginRequestDtoV2(
            String username,
            String password
    ) {

    public Authentication toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(
                this.username,
                this.password,
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_MEMBER")
                )
        );
    }
}