package com.java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//this Lombok annotation generates a builder pattern for the class, allowing for fluent construction of objects using chained method calls. This can be useful when creating instances of product with a subset of its fields or in a different order.
@Builder
public class Product {

    private int productId;
    private String name;
    private int qty;
    private double price;
}
