package com.amcrest.unity.accounting.security.jwt;

import com.amcrest.unity.accounting.security.jwt.domain.BlacklistedJwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistedJwtTokenRepository extends JpaRepository<BlacklistedJwtToken, Long> {

    Optional<BlacklistedJwtToken> findByToken(String token);
}
