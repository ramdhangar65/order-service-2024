package com.app.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "order")
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL) // this will store only non-null value in DB
public class Order {
    @Id
    private Integer orderId;
    private String orderName;
    private double price;
    private Integer qty;
    private List<Category> categories;
}
