package BookHub.msordenes.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret:}")
    private String secret;

    @PostConstruct
    private void init() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Required property 'jwt.secret' is not set.");
        }
    }

    private Key getSigningKey() {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        } catch (IllegalArgumentException e) {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(parseAllClaims(token));
    }

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        try {
            parseAllClaims(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException
                 | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
