package com.amcrest.unity.accounting.user.domain;

import com.amcrest.unity.accounting.email.validation.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserConfirmedDto {

    private String firstName;
    private String lastName;

    @NotEmpty
    @ValidEmail
    private String email;
    private String jwtToken;
}
