package com.amcrest.unity.accounting.userDevice.domain;

import com.amcrest.unity.accounting.validation.ValidEnumValue;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDeviceDto {

    @NotNull
    @NotEmpty
    private String deviceName;

    @NotNull
    @NotEmpty
    private String serialNumber;

    @NotNull
    @NotEmpty
    @ValidEnumValue(enumClass = UserDeviceRoles.class)
    private String userDeviceRole;

    private String sct;
    private String username;
    private String password;
}
