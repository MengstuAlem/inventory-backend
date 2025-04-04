package com.example.demo.inventorylog;

import jakarta.persistence.*;


import java.time.Instant;
public class InventoryLogo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    @Enumerated(EnumType.STRING)
  //  private InventoryAction action;
    private Integer quantity;
    private Integer previousStock;
    private Integer newStock;
    private Instant timestamp;
}
