package com.example.demo.product;

import com.example.demo.Category.Category;
import com.example.demo.supplier.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stockQuantity;
    private Integer minStockQuantity;
    @Enumerated(EnumType.STRING)
    private  ProductStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    @ManyToOne(fetch = FetchType.EAGER)
    Category category;
    @ManyToOne
    Supplier supplier;


}
