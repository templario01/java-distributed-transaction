package templario01.io.authentication.adapter.output;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import templario01.io.authentication.domain.entity.UserEntity;
import templario01.io.authentication.domain.repository.TokenProvider;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JjwtTokenAdapter implements TokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(UserEntity user) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("accountId", user.getAccountId())
                .claim("roles", user.getRoles())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }
}