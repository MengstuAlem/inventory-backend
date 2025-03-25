package com.example.demo.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    public List<PurchaseOrder> findAll() {
        return purchaseOrderRepository.findAll();
    }

    public Optional<PurchaseOrder> findById(long id) {
        if (id<=0){
            throw new IllegalArgumentException("Invalid purchase order ID");
        }
        return purchaseOrderRepository.findById(id);
    }

    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        if (purchaseOrderRepository.existsById(purchaseOrder.getId())) {
            throw new IllegalArgumentException("Purchase Order already exists");
        }

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public void deleteById(long id) {
        if (id<=0){
            throw new IllegalArgumentException("Invalid purchase order ID");
        }
        if (!purchaseOrderRepository.existsById(id)) {
            throw new IllegalArgumentException("Purchase Order does not exist");
        }

        try {
            purchaseOrderRepository.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("purchase order not found");
        }
    }

    public PurchaseOrder update(long id,PurchaseOrder purchaseOrder) {
        if (!purchaseOrderRepository.existsById(id)) {
            throw new IllegalArgumentException("Purchase Order does not exist");
        }
        PurchaseOrder existingPurchaseOrder=purchaseOrderRepository.findById(id).get();
        existingPurchaseOrder.setId(purchaseOrder.getId());
        if (purchaseOrder.getTotalAmount()!=null){
            existingPurchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount());
        }
        if (purchaseOrder.getOrderDate()!=null){
            existingPurchaseOrder.setOrderDate(purchaseOrder.getOrderDate());
        }
        if (purchaseOrder.getSupplier()!=null){
            existingPurchaseOrder.setSupplier(purchaseOrder.getSupplier());

        }
        if (purchaseOrder.getCreatedDate()!=null){
            existingPurchaseOrder.setCreatedDate(purchaseOrder.getCreatedDate());
        }
        if (purchaseOrder.getUser()!=null){
            existingPurchaseOrder.setUser(purchaseOrder.getUser());
        }
        if (purchaseOrder.getStatus()!=null){
            existingPurchaseOrder.setStatus(purchaseOrder.getStatus());
        }

        return purchaseOrderRepository.save(existingPurchaseOrder);
    }
}
