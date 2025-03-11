package com.timur.taskmanagement.jwt;

import com.timur.taskmanagement.enums.RoleUser;
import com.timur.taskmanagement.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private final SecretKey jwtAccessSecret;

    public JwtUtils(@Value("${jwt.secret.access}") String jwtAccessSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }

    public String generateAccessToken(User user) {

        final LocalDateTime now = LocalDateTime.now();
        final Instant instant = now.plusMinutes(25).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(instant);
        final List<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        String jwtAccessToken = Jwts.builder().setSubject(user.getEmail())
                .claim("userId", user.getId().toString())
                .claim("roles", roles)
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .compact();

        return jwtAccessToken;
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    private boolean validateToken(String jwtToken, Key secret) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (ExpiredJwtException expEx) {
            System.err.println("Token expired: " + expEx);
        } catch (UnsupportedJwtException unsEx) {
            System.err.println("Unsupported jwt: " + unsEx);
        } catch (MalformedJwtException mjEx) {
            System.err.println("Malformed jwt: " + mjEx);
        } catch (SignatureException sEx) {
            System.err.println("Invalid signature: " + sEx);
        } catch (Exception e) {
            System.err.println("Invalid token: " + e);
        }
        return false;
    }

    public Claims getClaimsFromJwtAccessToken(String jwtAccessToken) {
        return getClaimsFromJwtToken(jwtAccessToken, jwtAccessSecret);

    }

    private Claims getClaimsFromJwtToken(String jwt, Key key) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
    }
}

