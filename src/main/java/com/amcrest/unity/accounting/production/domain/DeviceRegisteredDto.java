package com.amcrest.unity.accounting.production.domain;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRegisteredDto {

    @NotNull
    @NonNull
    private String serialNumber;

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
