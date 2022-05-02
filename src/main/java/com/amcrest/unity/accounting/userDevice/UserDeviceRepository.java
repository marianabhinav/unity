package com.amcrest.unity.accounting.userDevice;

import com.amcrest.unity.accounting.userDevice.domain.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserDeviceRepository extends JpaRepository<UserDevice, String>{

    Optional<UserDevice> findByDeviceId(String deviceId);

    Optional<UserDevice> findBySerialNumber(String serialNumber);

    @Query("SELECT ud FROM UserDevice ud WHERE ud.isEnabled = true AND ud.serialNumber = ?1 AND ud.email = ?2")
    Optional<UserDevice> findBySerialNumberAndEmail(String serialNumber, String email);

    @Query("SELECT ud FROM UserDevice ud WHERE ud.isEnabled = true AND ud.email = ?1")
    List<UserDevice> findAllByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserDevice ud SET ud.isEnabled = false WHERE ud.serialNumber = ?1")
    int disableDevice(String serialNumber);
}
