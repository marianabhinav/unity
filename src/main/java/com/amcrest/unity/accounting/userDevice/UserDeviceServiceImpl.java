package com.amcrest.unity.accounting.userDevice;

import com.amcrest.unity.accounting.production.DeviceSNRepository;
import com.amcrest.unity.accounting.production.domain.DeviceSNProduction;
import com.amcrest.unity.accounting.user.domain.User;
import com.amcrest.unity.accounting.userDevice.domain.*;
import com.amcrest.unity.camera.config.SdkParams;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserDeviceServiceImpl implements UserDeviceService {

    private final DeviceSNRepository deviceSNRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final SdkParams sdkParams;
    private final ModelMapper modelMapper;

    @Override
    public void addDevice(AddUserDeviceDto userDeviceDto) {
        DeviceSNProduction deviceSNProduction = deviceSNRepository.findBySerialNumber(userDeviceDto.getSerialNumber())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Invalid serial number."
                        ));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserDevice> userDevice = userDeviceRepository.findBySerialNumber(userDeviceDto.getSerialNumber());
        if(userDevice.isPresent()){
            if(userDevice.get().getEmail().equals(user.getEmail())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This device already registered for this user.");
            }
            userDeviceRepository.disableDevice(userDevice.get().getSerialNumber());
        }
        UserDevice newUserDevice = UserDevice.builder()
                .email(user.getEmail())
                .deviceId(deviceSNProduction.getDeviceId())
                .deviceName(userDeviceDto.getDeviceName())
                .serialNumber(userDeviceDto.getSerialNumber())
                .userDeviceRole(userDeviceDto.getUserDeviceRole())
                .sct(userDeviceDto.getSct())
                .username(userDeviceDto.getUsername())
                .password(userDeviceDto.getPassword())
                .build();
        userDeviceRepository.save(newUserDevice);
    }

    @Override
    public GetUserDeviceListDto getUserDeviceList() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserDevice> userDevices = userDeviceRepository.findAllByEmail(user.getEmail());
        if(userDevices.isEmpty()){
            return GetUserDeviceListDto.builder().devices(new ArrayList<>()).build();
        }
        List<GetUserDeviceDto> getUserDeviceDtos = new ArrayList<>();
        for(UserDevice userDevice : userDevices){
            GetUserDeviceDto getUserDeviceDto = modelMapper.map(userDevice, GetUserDeviceDto.class);
            getUserDeviceDto.setServerKey(sdkParams.getServerKey());
            getUserDeviceDto.setProductId(sdkParams.getProductId());
            getUserDeviceDtos.add(getUserDeviceDto);
        }
        return GetUserDeviceListDto.builder().devices(getUserDeviceDtos).build();
    }

    @Override
    public GetUserDeviceDetailsDto getUserDeviceDetails(String serialNumber) {
        DeviceSNProduction deviceSNProduction = deviceSNRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Invalid serial number."
                        ));
        GetUserDeviceDetailsDto getUserDeviceDetailsDto = modelMapper.map(deviceSNProduction, GetUserDeviceDetailsDto.class);
        getUserDeviceDetailsDto.setServerKey(sdkParams.getServerKey());
        getUserDeviceDetailsDto.setProductId(sdkParams.getProductId());
        return getUserDeviceDetailsDto;
    }

    @Override
    public void deleteUserDevice(String serialNumber) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userDeviceRepository.findBySerialNumberAndEmail(serialNumber, user.getEmail()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Invalid serial number."
                ));
        userDeviceRepository.disableDevice(serialNumber);
    }
}
