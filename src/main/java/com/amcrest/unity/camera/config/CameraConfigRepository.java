package com.amcrest.unity.camera.config;

import com.amcrest.unity.camera.config.domain.CameraConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CameraConfigRepository extends JpaRepository<CameraConfig, String>{

    Optional<CameraConfig> findBySerialNumber(String serialNumber);
}
