package io.keede.bootlateststarter.security.v2.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.keede.bootlateststarter.domains.user.dto.AuthenticationDetail;
import io.keede.bootlateststarter.domains.user.entity.User;
import io.keede.bootlateststarter.domains.user.entity.UserRepository;
import io.keede.bootlateststarter.security.v2.dto.LoginRequestDtoV2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author kyh
 * Created on 2024/01/23
 */
@Component
public final class JwtTokenProvider implements InitializingBean {

    private final UserRepository userRepository;
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private SecretKey key;

    public JwtTokenProvider(
            final UserRepository userRepository,
            @Value("#{environment['jwt.secret']}")
            final String secret,
            @Value("#{environment['jwt.token-validity-in-seconds']}")
            final long tokenValidityInSeconds
    ) {
        this.userRepository = userRepository;
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = tokenValidityInSeconds * 1000; // 토큰 만료시간에 사용,
        this.refreshTokenValidityInMilliseconds = tokenValidityInSeconds * 5000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // NOTE : Presentation Layer를 통한 로그인을 할 떄 예시 코드
    public Token createJwtToken(
            final LoginRequestDtoV2 loginDto
    ) {

        User user = this.userRepository.findUserByUsername(loginDto.username())
                .orElseThrow();

        Date createdAt = new Date();

        Date accessTokenExpiredTime = new Date(createdAt.getTime() + this.accessTokenValidityInMilliseconds);
        Date refreshTokenExpiredTime = new Date(createdAt.getTime() + this.refreshTokenValidityInMilliseconds);

        // TODO : password 유효성 검사
        String accessToken = Jwts.builder()
                .issuedAt(createdAt)
                .signWith(this.key, Jwts.SIG.HS256)
                .issuer(user.getUsername())
                .expiration(accessTokenExpiredTime)
                .compact();

        String refreshToken = Jwts.builder()
                .issuedAt(createdAt)
                .signWith(this.key, Jwts.SIG.HS256)
                .issuer(user.getUsername())
                .expiration(refreshTokenExpiredTime)
                .compact();

        return new Token(
                accessToken,
                refreshToken
        );
    }

    // NOTE : Security Filter를 통한 로그인을 할 떄 예시 코드
    public Token createJwtToken(
            final AuthenticationDetail loginDto
    ) {

        User user = this.userRepository.findUserByUsername(loginDto.getUsername())
                .orElseThrow();

        Date createdAt = new Date();

        Date accessTokenExpiredTime = new Date(createdAt.getTime() + this.accessTokenValidityInMilliseconds);
        Date refreshTokenExpiredTime = new Date(createdAt.getTime() + this.refreshTokenValidityInMilliseconds);

        // TODO : password 유효성 검사
        String accessToken = Jwts.builder()
                .issuedAt(createdAt)
                .signWith(this.key, Jwts.SIG.HS256)
                .issuer(user.getUsername())
                .expiration(accessTokenExpiredTime)
                .compact();

        String refreshToken = Jwts.builder()
                .issuedAt(createdAt)
                .signWith(this.key, Jwts.SIG.HS256)
                .issuer(user.getUsername())
                .expiration(refreshTokenExpiredTime)
                .compact();

        return new Token(
                accessToken,
                refreshToken
        );
    }

    public String bindAuthorizationToken(
            final String token
    ) {
        try {
            return Jwts.parser()
                    .verifyWith(this.key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getIssuer();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            e.printStackTrace();
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }

        return null;
    }

}
