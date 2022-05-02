package com.amcrest.unity.accounting.adminUser.domain;

import com.amcrest.unity.accounting.email.validation.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockUserDto {

    @NotNull
    @NotEmpty
    @ValidEmail
    String email;

    @NotNull
    private Boolean isAccountNonLocked;
}
