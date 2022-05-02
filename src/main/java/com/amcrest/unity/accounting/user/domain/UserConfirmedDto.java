package com.amcrest.unity.accounting.user.domain;

import com.amcrest.unity.accounting.email.validation.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserConfirmedDto {

    private String firstName;
    private String lastName;

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    @NotNull
    @NotEmpty
    private String fingerprint;

    private String jwtToken;
}
