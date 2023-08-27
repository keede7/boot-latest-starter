package io.keede.bootlateststarter.domains.user.service;

import io.keede.bootlateststarter.domains.user.dto.AuthenticationDetail;
import io.keede.bootlateststarter.domains.user.entity.User;
import io.keede.bootlateststarter.domains.user.entity.UserRepository;
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

    private final UserRepository userRepository;

    public BootUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        return new AuthenticationDetail(username, user.getPassword());
    }
}
