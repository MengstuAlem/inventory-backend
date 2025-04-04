package com.example.demo.stock;

import com.example.demo.User.UserEntity;
import com.example.demo.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private StockType type;
    private Instant transactionDate;
    private String reference;
    private Instant createdAt;
    @ManyToOne
    @JsonIgnoreProperties(value = { "category", "supplier" }, allowSetters = true)
    private Product product;
    @ManyToOne
    private UserEntity user;

}
