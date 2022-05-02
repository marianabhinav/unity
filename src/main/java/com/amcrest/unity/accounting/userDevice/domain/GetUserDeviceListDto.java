package com.amcrest.unity.accounting.userDevice.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDeviceListDto {

    @NotNull
    @NotEmpty
    private List<GetUserDeviceDto> devices;
}
