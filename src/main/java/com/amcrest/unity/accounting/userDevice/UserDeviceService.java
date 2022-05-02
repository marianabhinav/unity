package com.amcrest.unity.accounting.userDevice;

import com.amcrest.unity.accounting.userDevice.domain.AddUserDeviceDto;
import com.amcrest.unity.accounting.userDevice.domain.GetUserDeviceDetailsDto;
import com.amcrest.unity.accounting.userDevice.domain.GetUserDeviceDto;
import com.amcrest.unity.accounting.userDevice.domain.GetUserDeviceListDto;

public interface UserDeviceService {

    void addDevice(AddUserDeviceDto addUserDeviceDto);

    GetUserDeviceListDto getUserDeviceList();

    GetUserDeviceDetailsDto getUserDeviceDetails(String serialNumber);

    void deleteUserDevice(String serialNumber);
}
