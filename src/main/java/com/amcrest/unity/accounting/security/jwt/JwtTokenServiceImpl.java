package com.amcrest.unity.accounting.security.jwt;

import com.amcrest.unity.accounting.user.domain.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final BlacklistedJwtTokenRepository blacklistedJwtTokenRepository;

    @Override
    public String createJwtToken(User user) {
        String token = Jwts.builder()
                .claim(jwtConfig.getClaimUserEmail(), user.getEmail())
                .claim(jwtConfig.getClaimUserScopes(), user.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
        return jwtConfig.getTokenPrefix() + token;
    }

    @Override
    public boolean verifyJwtToken(String jwtToken) {
        String token = jwtToken.replace(jwtConfig.getTokenPrefix(), "");
        Optional.of(blacklistedJwtTokenRepository.findByToken(token))
                .ifPresent(e -> {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.");
                });

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.");
        }
        return true;
    }

    @Override
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get(jwtConfig.getClaimUserEmail())
                .toString();
    }
}
