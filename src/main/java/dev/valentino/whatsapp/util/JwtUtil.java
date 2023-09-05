package dev.valentino.whatsapp.util;

import dev.valentino.whatsapp.auth.AuthToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtil {

    public static SecretKey SECRET_KEY = Keys.hmacShaKeyFor("iN4B90NEWEKAGSKDNHKLSFNHKFADKNMEHYDFNHMDFHADFDdskgnsd".getBytes(StandardCharsets.UTF_8));

    public static String createJwt(AuthToken authentication) {
        return Jwts.builder()
                .signWith(SECRET_KEY)
                .claim("id", authentication.getUser().getId())
                .claim("authorities", authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(", ")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Duration.ofDays(1L).toMillis()))
                .compact();
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
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findAny()
                .orElse(null);
    }
}
