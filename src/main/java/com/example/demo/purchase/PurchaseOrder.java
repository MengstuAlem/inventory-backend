package com.example.demo.purchase;

import com.example.demo.User.ProfileOfUser;
import com.example.demo.product.Product;
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
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    private Instant orderDate;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private PurchaseOrderStutas status;
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfileOfUser user;

}
