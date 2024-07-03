package io.keede.bootlateststarter.domains.user.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @author kyh
 * Created on 2024/01/24
 */
public class AuthenticationDetail extends User implements OAuth2User {

    private String username;
    private Map<String, Object> attributes;

    public AuthenticationDetail(
            final String username,
            final String password,
            final String role
    ) {
        this(
                username,
                password,
                Collections.singletonList(
                        new SimpleGrantedAuthority(role)
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

    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
            this.attributes.put("name", this.getName());
        }
        return attributes;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
