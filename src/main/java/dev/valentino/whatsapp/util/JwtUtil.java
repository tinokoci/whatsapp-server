package dev.valentino.whatsapp.util;

import dev.valentino.whatsapp.auth.AuthToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

public class JwtUtil {

    public static SecretKey SECRET_KEY = Keys.hmacShaKeyFor("iN4B90NEWEKAGSKDNHKLSFNHKFADKNMEHYDFNHMDFHADFDdskgnsd".getBytes(StandardCharsets.UTF_8));
    public static final String AUTH_HEADER = "Authorization";

    public static String createJwt(AuthToken authentication) {
        return Jwts.builder()
                .signWith(SECRET_KEY)
                .claim("id", authentication.getId())
                .claim("username", authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Duration.ofDays(1L).toMillis()))
                .compact();
    }

    public static String getUsernameFromJwt(String jwt) {
        return getClaimFromJwt(jwt, "username", String.class);
    }

    public static <T> T getClaimFromJwt(String jwt, String claimName, Class<T> clazz) {
        Claims claims = getClaimsFromJwt(jwt);
        if (claims == null) return null;
        return claims.get(claimName, clazz);
    }

    public static Claims getClaimsFromJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    @Nullable
    public static String getJwtTokenFromRequest(HttpServletRequest request) {
        String jwt = request.getHeader(AUTH_HEADER);
        if (jwt == null) return null;

        try {
            // JWT example: "Bearer actualtoken12345
            // substring(7) removes the "Bearer " part
            return jwt.substring(7);
        } catch (Exception e) {
            return null;
        }
    }
}
