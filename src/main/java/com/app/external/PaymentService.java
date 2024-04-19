package com.app.external;

import com.app.collection.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value="payment-service",url = "http://localhost:8082/payments")
public interface PaymentService {
    @GetMapping("/paymentDetails")
     List<Payment> getListOfPaymentDetails();


}
