package com.amcrest.unity.camera.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.sdk")
@RequiredArgsConstructor
@Getter
@Setter
public class SdkParams {
    private Integer idLength;
    private String productId;
    private String serverKey;
}
