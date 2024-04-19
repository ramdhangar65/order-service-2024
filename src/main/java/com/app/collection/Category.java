package com.app.collection;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {
    private String mobiles;
    private String grocery;
    private String fashion;
    private String electronics;
    private String sportsHub;

}
