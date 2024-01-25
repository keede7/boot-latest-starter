package io.keede.bootlateststarter.domains.user.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;


/**
 * @author kyh
 * Created on 2024/01/24
 */
public class AuthenticationDetail extends User {

    private String username;

    public AuthenticationDetail(
            final String username,
            final String password
    ) {
        this(
                username,
                password,
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_MEMBER")
                )
        );
        this.username = username;
    }

    public AuthenticationDetail(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
