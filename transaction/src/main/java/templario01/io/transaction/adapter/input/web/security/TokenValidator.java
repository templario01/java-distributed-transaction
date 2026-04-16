package templario01.io.transaction.adapter.input.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class TokenValidator {

    @Value("${jwt.secret}")
    private String secret;

    public String getAccountIdFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("accountId", String.class);
    }
}

/* ALTERNATIVE 2: Using Spring Security
@Component
public class TokenValidator {

    @Value("${jwt.secret}")
    private String secret;

    public String getAccountIdFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("accountId", String.class);
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("roles", List.class);
    }

    private Claims getAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}*/