package com.amcrest.unity.accounting.adminUser.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistedJwtDto {

    @NotNull
    @NotEmpty
    String token;
}
