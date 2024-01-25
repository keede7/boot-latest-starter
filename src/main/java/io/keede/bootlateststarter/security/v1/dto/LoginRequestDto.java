package io.keede.bootlateststarter.security.v1.dto;

/**
 * Security 인증 객체
 * @author keede
 * Created on 2023/08/23
 */
public record LoginRequestDto(
        String username,
        String password
) {
}
