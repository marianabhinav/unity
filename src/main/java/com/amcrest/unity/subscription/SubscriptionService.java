package com.amcrest.unity.subscription.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.math.BigDecimal;

public interface SubscriptionService {

    String getClientToken();
    Boolean makePayment(BigDecimal totalCost, String paymentMethodNonce);
}
