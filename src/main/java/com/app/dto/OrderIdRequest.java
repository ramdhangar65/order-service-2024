package com.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderIdRequest {
    private List<Integer> orderIds;
    private List<String> orderCategories;
}
