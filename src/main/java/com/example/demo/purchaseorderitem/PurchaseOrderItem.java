package com.example.demo.purchaseorderitem;

import com.example.demo.product.Product;
import com.example.demo.purchase.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    private Integer quantity;
    private BigDecimal price;
    private Integer receivedQuantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "supplier", "createdBy" }, allowSetters = true)
    private PurchaseOrder purchaseOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category", "supplier" }, allowSetters = true)
    private Product product;

}
