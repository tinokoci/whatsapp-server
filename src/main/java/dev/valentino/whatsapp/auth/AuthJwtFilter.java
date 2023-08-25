package dev.valentino.whatsapp.auth;

import dev.valentino.whatsapp.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class AuthJwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = JwtUtil.getJwtTokenFromRequest(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // Convert jwt string to an actual object
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JwtUtil.SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        // Fail if token is expired
        if (claims.getExpiration().before(new Date())) {
            filterChain.doFilter(request, response);
            return;
        }
        // Get user data from JWT token
        String username = claims.getSubject();
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get("authorities", String.class));

        // Set authentication to spring context
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        System.out.println(request.getServletPath());
        return Stream.of("/public", "/api/v1/auth")
                .anyMatch(path -> request.getServletPath().startsWith(path));
    }
}
