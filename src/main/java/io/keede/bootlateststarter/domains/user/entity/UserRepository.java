package io.keede.bootlateststarter.domains.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
* @author keede
* Created on 2023/08/22
*/
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user " +
            "from User user " +
            "where user.username = :username ")
    Optional<User> findUserByUsername(@Param("username") String username);

}
