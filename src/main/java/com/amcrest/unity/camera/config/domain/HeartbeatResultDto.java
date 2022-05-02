package com.amcrest.unity.camera.config.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeartbeatResultDto {
    @NotNull
    @NotEmpty
    private boolean locked;
}
