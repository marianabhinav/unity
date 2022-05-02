package com.amcrest.unity.storage.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class StorageConfig {
//
//    @Value("${spring.cloud.aws.credentials.access-key}")
//    private String access_key;
//
//    @Value("${spring.cloud.aws.credentials.secret-key}")
//    private String access_secret;
//
//    @Value("${spring.cloud.aws.region.static}")
//    private String region;
//
//    @Bean
//    public AmazonS3 s3Client() {
//        AWSCredentials credentials = new BasicAWSCredentials(access_key,access_secret);
//        return AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
//    }
//}


@Configuration
@ConfigurationProperties(prefix = "application.sdk")
@RequiredArgsConstructor
@Getter
@Setter
public class StorageConfig {
    private Integer idLength;
    private String productId;
    private String serverKey;
}
