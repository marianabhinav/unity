package com.amcrest.unity.accounting.production.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DeviceSNProduction {

    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Schema(hidden = true)
    private String serialNumber;

    @Schema(hidden = true)
    @NotNull
    @NonNull
    private String deviceId;

    @NotNull
    @NonNull
    private String variantId;

    @NotNull
    @NonNull
    private String providerId;
}