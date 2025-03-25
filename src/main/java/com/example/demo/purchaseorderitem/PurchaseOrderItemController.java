package com.example.demo.purchaseorderitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("purchase-order-items")
public class PurchaseOrderItemController {
    @Autowired
    private  PurchaseOrderItemService purchaseOrderItemService;

    @GetMapping
    public ResponseEntity<List<PurchaseOrderItem>> findAll(){

        return ResponseEntity.ok(purchaseOrderItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderItem> findById(@PathVariable Long id){
        return ResponseEntity.ok(purchaseOrderItemService.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order item does not exist")));
    }
    @PostMapping
    public ResponseEntity<PurchaseOrderItem> save(@RequestBody PurchaseOrderItem purchaseOrderItem){
        return ResponseEntity.ok(purchaseOrderItemService.save(purchaseOrderItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        purchaseOrderItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    public ResponseEntity<PurchaseOrderItem> update(@RequestBody PurchaseOrderItem purchaseOrderItem){
        return ResponseEntity.ok(purchaseOrderItemService.save(purchaseOrderItem));
    }

}
