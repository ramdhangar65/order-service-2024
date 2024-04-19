package com.app.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment")
public class Payment {
    @Id
    private String paymentId;
    private String  transactionId;
    private String message;
    private Integer orderId;
    private String status;
}
