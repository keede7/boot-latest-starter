package io.keede.bootlateststarter.domains.user.service;

import io.keede.bootlateststarter.domains.user.entity.User;
import io.keede.bootlateststarter.domains.user.entity.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author kyh
 * Created on 2024/01/23
 */
@Service
public class UserReader {

    private final UserRepository userRepository;

    public UserReader(
            final UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(
            final String username
    ) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("존재하지 않는 사용자입니다.")
                );
    }
}
