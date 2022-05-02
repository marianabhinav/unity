package com.amcrest.unity.accounting.production;

import com.amcrest.unity.accounting.production.domain.DeviceSNProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface DeviceSNRepository extends JpaRepository<DeviceSNProduction, String> {

    Optional<DeviceSNProduction> findBySerialNumber(String serialNumber);
}
