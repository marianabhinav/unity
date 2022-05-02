package com.amcrest.unity.accounting.production;

import com.amcrest.unity.accounting.production.domain.DeviceRegisteredDto;
import com.amcrest.unity.accounting.production.domain.DeviceRegistrationDto;

public interface DeviceRegistrationService {

    DeviceRegisteredDto registerDevice(DeviceRegistrationDto deviceRegistrationDto);

}
