package io.keede.bootlateststarter.domains.user;

import io.keede.bootlateststarter.domains.user.entity.User;
import io.keede.bootlateststarter.domains.user.entity.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 * @author keede
 * Created on 2023/08/22
 */
@Repository
public class DataBaseInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataBaseInitializer(
            final UserRepository userRepository,
            final PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    void init() {
        User user = new User(
                "tester",
                passwordEncoder.encode("1212")
        );

        User save = userRepository.save(user);
        System.out.println("save.getId() = " + save.getId());
        System.out.println("save = " + save.getUsername());
        System.out.println("save = " + save.getPassword());
    }

}
