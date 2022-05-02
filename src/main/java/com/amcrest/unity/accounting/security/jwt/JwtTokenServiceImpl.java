package com.amcrest.unity.accounting.security.jwt;

import com.amcrest.unity.accounting.security.jwt.domain.BlacklistedJwtToken;
import com.amcrest.unity.accounting.user.domain.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        String jwtToken = Jwts.builder()
                .claim(jwtConfig.getClaimUserEmail(), user.getEmail())
                .claim(jwtConfig.getClaimUserScopes(), user.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
        return jwtConfig.getTokenPrefix() + " " + jwtToken;
    }

    @Override
    public boolean verifyJwtToken(String jwtToken) {
        //TODO: change blacklisted token to cache.
        Optional<BlacklistedJwtToken> blacklistedJwtToken = (blacklistedJwtTokenRepository.findByToken(jwtToken));
        if(blacklistedJwtToken.isPresent()) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                    | SignatureException | IllegalArgumentException e) {
           return false;
        }
        return true;
    }

    @Override
    public String getUserNameFromJwtToken(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody()
                .get(jwtConfig.getClaimUserEmail())
                .toString();
    }
}
