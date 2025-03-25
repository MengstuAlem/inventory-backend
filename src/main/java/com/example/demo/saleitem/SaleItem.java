package com.example.demo.saleitem;

import com.example.demo.product.Product;
import com.example.demo.sale.Sale;
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
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    private Integer quantity;
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ProfileOfUser" }, allowSetters = true)
    private Sale sale;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category", "supplier" }, allowSetters = true)
    private Product product;

}
