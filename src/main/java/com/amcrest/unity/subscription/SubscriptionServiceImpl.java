package com.amcrest.unity.subscription.service;

import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amcrest.unity.accounting.response.ResponseHandler;
import com.amcrest.unity.storage.config.AWSS3ClientConfig;
import com.amcrest.unity.subscription.config.BraintreeConfig;
import com.braintreegateway.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;


@Service
@Transactional
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private BraintreeGateway braintreeGateway;

    private final BraintreeConfig braintreeConfig;

    @Override
    public String getClientToken() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest();
        return braintreeGateway.clientToken().generate(clientTokenRequest);
    }

    @Override
    public Boolean makePayment(BigDecimal totalCost, String paymentMethodNonce) {
        String transactionId = processPayment(totalCost, paymentMethodNonce);
        if (transactionId != null) {
            return true;
        }
        return false;
    }

//    public Boolean makePayment(Long orderId, BigDecimal totalCost, String paymentMethodNonce) {
//        String transactionId = processPayment(totalCost, paymentMethodNonce);
//        if (transactionId != null) {
//            insertPayment(orderId, transactionId);
//            return true;
//        }
//        return false;
//    }

//    private void insertPayment(Long orderId, String transactionId) {
//        Payment payment = new Payment();
//        payment.setDateCreated(new Date());
//        payment.setTransactionId(transactionId);
//        payment.setOrderId(orderId);
//        paymentRepository.save(payment);
//    }

    private String processPayment(BigDecimal totalCost, String paymentMethodNonce) {
        Result<Transaction> result = this.processTransaction(totalCost, paymentMethodNonce);

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            System.out.println("Success!: " + transaction.getId());
            String transactionId = transaction.getId();
            System.out.println(transactionId);
            return transactionId;
        } else if (result.getTransaction() != null) {
            Transaction transaction = result.getTransaction();
            System.out.println("Failed!: " + transaction.getId());
            System.out.println("Error processing transaction:");
            System.out.println("  Status: " + transaction.getStatus());
            System.out.println("  Code: " + transaction.getProcessorResponseCode());
            System.out.println("  Text: " + transaction.getProcessorResponseText());
            return null;
        } else {
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                System.out.println("Attribute: " + error.getAttribute());
                System.out.println("  Code: " + error.getCode());
                System.out.println("  Message: " + error.getMessage());
            }
            return null;
        }
    }

    private Result<Transaction> processTransaction(BigDecimal totalCost, String paymentMethodNonce) {
        TransactionRequest req = new TransactionRequest().amount(totalCost).paymentMethodNonce(paymentMethodNonce)
                .options().submitForSettlement(true).done();

        Result<Transaction> result = braintreeGateway.transaction().sale(req);
        System.out.println(result.getMessage());
        return result;
    }
}