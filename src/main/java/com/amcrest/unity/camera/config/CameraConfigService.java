package com.amcrest.unity.camera.config;

import com.amcrest.unity.camera.config.domain.HeartbeatResultDto;
import com.amcrest.unity.camera.config.domain.SyncDataDto;

public interface CameraConfigService {

    SyncDataDto syncConfiguration(String deviceId, Long timestamp);

    HeartbeatResultDto heartbeat(String email);
}
