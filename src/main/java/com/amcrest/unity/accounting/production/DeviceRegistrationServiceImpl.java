package com.amcrest.unity.accounting.production;

import com.amcrest.unity.accounting.production.domain.DeviceRegisteredDto;
import com.amcrest.unity.accounting.production.domain.DeviceRegistrationDto;
import com.amcrest.unity.accounting.production.domain.DeviceSNProduction;
import com.amcrest.unity.camera.config.SdkParams;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DeviceRegistrationServiceImpl implements DeviceRegistrationService {

    private final DeviceSNRepository deviceSNRepository;
    private final ModelMapper modelMapper;
    private final SdkParams sdkParams;

    @Override
    public DeviceRegisteredDto registerDevice(DeviceRegistrationDto deviceRegistrationDto) {
        String format = "SN%1$s%2$s%3$09d";
        //TODO: get device Id from SDK.
        String deviceId = "de-uiwyzzbv" + Instant.now().getEpochSecond();
        String deviceIdPart = deviceId.substring(deviceId.length()-4);
        String providerId = deviceRegistrationDto.getProviderId();
        String providerIdPart = providerId.substring(0,3);

        DeviceSNProduction deviceSNProduction = DeviceSNProduction.builder()
                .deviceId(deviceId)
                .providerId(providerId)
                .variantId(sdkParams.getProductId())
                .build();
        deviceSNProduction = deviceSNRepository.save(deviceSNProduction);
        String serialNumber = String.format(format, providerIdPart, deviceIdPart, deviceSNProduction.getId());
        deviceSNProduction.setSerialNumber(serialNumber);
        return modelMapper.map(deviceSNRepository.save(deviceSNProduction), DeviceRegisteredDto.class);
    }
}
