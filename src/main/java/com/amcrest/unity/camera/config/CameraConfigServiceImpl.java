package com.amcrest.unity.camera.config;

import com.amcrest.unity.accounting.user.UserRepository;
import com.amcrest.unity.accounting.user.domain.User;
import com.amcrest.unity.accounting.userDevice.UserDeviceRepository;
import com.amcrest.unity.accounting.userDevice.domain.UserDevice;
import com.amcrest.unity.camera.config.domain.CameraConfig;
import com.amcrest.unity.camera.config.domain.HeartbeatResultDto;
import com.amcrest.unity.camera.config.domain.SyncDataDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CameraConfigServiceImpl implements CameraConfigService {

    private final UserDeviceRepository userDeviceRepository;
    private final CameraConfigRepository cameraConfigRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SdkParams sdkParams;

    @Override
    public SyncDataDto syncConfiguration(String encodedDeviceId, Long timestamp) {
        String deviceId = decodeDeviceId(encodedDeviceId);
        UserDevice userDevice = userDeviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Device ID is invalid."
                        )
                );
        Optional<CameraConfig> cameraConfig = cameraConfigRepository.findBySerialNumber(userDevice.getSerialNumber());
        if(cameraConfig.isPresent()){
            if(cameraConfig.get().getTimestamp().equals(timestamp)){
                return null;
            }

            String config = cameraConfig.get().getConfig().replace("\\","");
            if(config.charAt(0) == '"' && config.charAt(config.length()-1) == '"' ) {
                config = config.substring(1, config.length() - 1);
            }
            Map<String, Object> configMap = mapObject(new ByteArrayInputStream(config.getBytes()));
            return SyncDataDto.builder()
                        .config(configMap)
                        .timestamp(cameraConfig.get().getTimestamp())
                        .email(userDevice.getEmail())
                        .build();
        }
        else {
            Map<String, Object> config = getConfigJson(userDevice.getEmail());
            Long currentTimestamp = Instant.now().getEpochSecond();
            String configString = "";
            try {
                configString = new ObjectMapper().writeValueAsString(config);
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE, "Cannot create config.");
            }
            CameraConfig createCameraConfig = CameraConfig.builder()
                            .timestamp(currentTimestamp)
                            .serialNumber(userDevice.getSerialNumber())
                            .config(configString)
                            .build();
            cameraConfigRepository.save(createCameraConfig);
            SyncDataDto syncDataDto = modelMapper.map(createCameraConfig, SyncDataDto.class);
            syncDataDto.setEmail(userDevice.getEmail());
            syncDataDto.setConfig(config);
            return syncDataDto;
        }
    }

    @Override
    public HeartbeatResultDto heartbeat(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found."
                        )
                );
        if(user.getIsAccountNonExpired() && user.getIsAccountNonLocked()){
            return HeartbeatResultDto.builder().locked(false).build();
        }
        else{
            return HeartbeatResultDto.builder().locked(true).build();
        }
    }

    private Map<String, Object> getConfigJson(String email){
        //TODO: get config on user subscription base.
        ClassLoader classLoader = CameraConfigServiceImpl.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream("generic_default_config.json");
       if(is != null){
            return mapObject(is);
       }
       else{
           throw new ResponseStatusException(
                   HttpStatus.SERVICE_UNAVAILABLE, "Cannot create config."
           );
       }
    }

    private Map<String, Object> mapObject(InputStream is){
        TypeReference<LinkedHashMap<String,Object>> typeRef = new TypeReference<>(){};
        try {
            return new ObjectMapper().readValue(is, typeRef);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "Cannot create config.");
        }
    }

    private String decodeDeviceId(String encodedDeviceId){
        Integer key = Character.getNumericValue(encodedDeviceId.charAt(encodedDeviceId.length()-1));
        Integer startIdx =  getKeyMapper().get(key);
        if( startIdx == null || (startIdx + sdkParams.getIdLength()) > encodedDeviceId.length()-1){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device ID is invalid.");
        }
        return encodedDeviceId.substring(startIdx, startIdx + sdkParams.getIdLength());
    }

    private Map<Integer, Integer> getKeyMapper(){
        return new LinkedHashMap<>() {{
        put(0,3);
        put(1,4);
        put(2,0);
        put(3,2);
        put(4,5);
        put(5,1);
        }};
    }
}
