package io.keede.bootlateststarter.domains.user.entity;

import jakarta.persistence.*;

/**
* @author keede
* Created on 2023/08/22
*/
@Entity
@Table(name = "user_t")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    protected User() {}

    public User(
            final String username,
            final String password
    ) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return this.password;
    }
}
