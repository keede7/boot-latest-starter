package io.keede.bootlateststarter.security.v2.dto;

/**
 * @author kyh
 * Created on 2024/01/24
 */
public record LoginRequestDto(
            String username,
            String password
    ) {
}