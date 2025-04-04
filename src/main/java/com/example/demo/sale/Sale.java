package com.example.demo.sale;

import com.example.demo.User.UserEntity;
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
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    private String customerName;
    private Instant saleDate;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    SaleStatus status;
    private Instant createdAt;
    @ManyToOne
    UserEntity user;
}
