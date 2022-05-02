package com.amcrest.unity.subscription.controller;

import com.amcrest.unity.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path="api/v1/subscription")
public class SubscriptionController {

    private final SubscriptionService service;

    @PostMapping(path = "/subscribe")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment Completed Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    public void subscribe(
            @Valid @RequestParam(value = "email") String email,
            @Valid @RequestParam(value = "totalCost") BigDecimal totalCost){
//        return service.makePayment(totalCost,paymentMethodNonce);
    }


//    public Boolean subscribe(
//            @Valid @RequestParam(value = "email") String username,
//            @Valid @RequestParam(value = "totalCost") BigDecimal totalCost,
//            @Valid @RequestParam(value = "paymentMethodNonce") String paymentMethodNonce ){
//        return service.makePayment(totalCost,paymentMethodNonce);
//    }

}
