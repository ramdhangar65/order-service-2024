package com.app.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class OrderResponse {
    private String paymentId;
    private String  transactionId;
    private String message;
    private Integer orderId;
    private String status;
}
