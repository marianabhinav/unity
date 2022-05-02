package com.amcrest.unity.camera.config.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class CameraConfig {

    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @NotEmpty
    private String serialNumber;

    private Long timestamp;

    @NotNull
    @NotEmpty
    @Column(columnDefinition = "json")
    private String config;
}
