package com.amcrest.unity.camera.config.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyncDataDto {

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private Map<String, Object> config;

    @NotNull
    @NotEmpty
    private Long timestamp;
}
