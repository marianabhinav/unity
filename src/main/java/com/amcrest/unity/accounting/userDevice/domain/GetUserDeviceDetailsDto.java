package com.amcrest.unity.accounting.userDevice.domain;

import com.amcrest.unity.accounting.validation.ValidEnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDeviceDetailsDto {

    @NotNull
    @NotEmpty
    private String deviceId;

    @NotNull
    @NotEmpty
    private String serialNumber;

    @NotNull
    @NotEmpty
    private String productId;

    @NotNull
    @NotEmpty
    private String serverKey;
}
