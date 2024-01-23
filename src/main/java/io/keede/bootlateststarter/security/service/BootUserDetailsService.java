package io.keede.bootlateststarter.security.service;

import io.keede.bootlateststarter.domains.user.dto.AuthenticationDetail;
import io.keede.bootlateststarter.domains.user.entity.User;
import io.keede.bootlateststarter.domains.user.entity.UserRepository;
import io.keede.bootlateststarter.domains.user.service.UserReader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
* @author keede
* Created on 2023/08/22
*/
@Service
public class BootUserDetailsService implements UserDetailsService {

    private final UserReader userReader;

    public BootUserDetailsService(
            final UserReader userReader
    ) {
        this.userReader = userReader;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        User user = this.userReader.findUserByUsername(username);

        return new AuthenticationDetail(username, user.getPassword());
    }
}
