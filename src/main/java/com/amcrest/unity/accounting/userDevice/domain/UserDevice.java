package com.amcrest.unity.accounting.userDevice.domain;

import com.amcrest.unity.accounting.validation.ValidEnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class UserDevice {

    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Schema(hidden = true)
    @Builder.Default
    private boolean isEnabled = true;

    @NotNull
    @NotEmpty
    private String serialNumber;

    @Schema(hidden = true)
    private String email;

    @Schema(hidden = true)
    private String deviceId;

    @NotNull
    @NotEmpty
    @ValidEnumValue(enumClass = UserDeviceRoles.class)
    private String userDeviceRole;

    @NotNull
    @NotEmpty
    private String deviceName;

    private String sct;
    private String username;
    private String password;
}
