package com.amcrest.unity.accounting.user.domain;

import com.amcrest.unity.accounting.user.validation.ValidPassword;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePasswordDto {

    @NotNull
    @NotEmpty
    private Integer otp;

    @NotNull
    @NotEmpty
    @ValidPassword
    private String password;
}
