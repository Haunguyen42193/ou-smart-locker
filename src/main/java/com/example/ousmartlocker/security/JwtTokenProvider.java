package com.example.ousmartlocker.security;

import com.example.ousmartlocker.exception.OuSmartLockerBadRequestApiException;
import com.example.ousmartlocker.model.User;
import com.example.ousmartlocker.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecretKey;
    @Value("${app.jwt-expiration-milliseconds}")
    private Long expiration;
    private final UserRepository userRepository;

    @Autowired
    public JwtTokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();
        User user = userRepository.findByUsername(userName);
        Date expireDate = new Date(new Date().getTime() + expiration);
        return Jwts.builder()
                .setSubject(userName)
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    public String getUserName(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new OuSmartLockerBadRequestApiException("Invalid Token", e);
        } catch (ExpiredJwtException e) {
            throw new OuSmartLockerBadRequestApiException("Token Expired", e);
        } catch (UnsupportedJwtException e) {
            throw new OuSmartLockerBadRequestApiException("Unsupported token", e);
        } catch (IllegalArgumentException e) {
            throw new OuSmartLockerBadRequestApiException("Invalid argument", e);
        } catch (SignatureException e) {
            throw new OuSmartLockerBadRequestApiException("JWT signature does not match", e);
        }
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }
}
