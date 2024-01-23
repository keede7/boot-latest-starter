package io.keede.bootlateststarter.security.config.jwt;

/**
 * @author kyh
 * Created on 2024/01/23
 */
public record Token(
        String accessToken,
        String refreshToken
) {
}
