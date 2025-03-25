package com.example.demo.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("purchase-orders")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders(){

        return ResponseEntity.ok(purchaseOrderService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable long id){
        if (id<=0){
            return ResponseEntity.badRequest().build();
        }
        return  purchaseOrderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){
        if (purchaseOrder == null){
            return ResponseEntity.badRequest().build();
        }

       return ResponseEntity.ok(purchaseOrderService.save(purchaseOrder));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrderById(@PathVariable long id){
        if (id<=0){
            return ResponseEntity.badRequest().build();
        }
        purchaseOrderService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(@PathVariable long id, @RequestBody PurchaseOrder purchaseOrder){
        if (purchaseOrder == null){
            return ResponseEntity.badRequest().build();
        }
        if (id <=0){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(purchaseOrderService.update(id,purchaseOrder));
    }
}
