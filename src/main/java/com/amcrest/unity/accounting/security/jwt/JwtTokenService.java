package com.amcrest.unity.accounting.security.jwt;

import com.amcrest.unity.accounting.user.domain.User;

public interface JwtTokenService {

    String createJwtToken(User user);

    boolean verifyJwtToken(String jwtToken);

    String getUserNameFromJwtToken(String token);
}
