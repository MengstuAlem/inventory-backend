package com.example.demo.purchaseorderitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderItemService {

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    public List<PurchaseOrderItem> findAll() {
        return purchaseOrderItemRepository.findAll();
    }

    public Optional<PurchaseOrderItem> findById(long id) {
        if (id<=0){
            throw new RuntimeException("Invalid purchase order item ID");
        }
        return purchaseOrderItemRepository.findById(id);
    }

    public PurchaseOrderItem save(PurchaseOrderItem purchaseOrderItem) {
        if (purchaseOrderItem==null){
            throw new NullPointerException("Invalid purchase order item");
        }
        if (purchaseOrderItemRepository.findById(purchaseOrderItem.getId()).isPresent()){
            throw new RuntimeException("Purchase order item already exists");
        }
        return purchaseOrderItemRepository.save(purchaseOrderItem);
    }

    public void deleteById(long id) {
        if (id<=0){
            throw new RuntimeException("Invalid purchase order item ID");
        }
        if (purchaseOrderItemRepository.findById(id).isEmpty()){
            throw new RuntimeException("Purchase order item does not exist");
        }
        purchaseOrderItemRepository.deleteById(id);
    }

    public PurchaseOrderItem update(long id, PurchaseOrderItem purchaseOrderItem) {
        if (purchaseOrderItemRepository.findById(id).isEmpty()){
            throw new RuntimeException("Purchase order item does not exist");
        }
        PurchaseOrderItem exist = purchaseOrderItemRepository.findById(purchaseOrderItem.getId()).get();
        exist.setQuantity(purchaseOrderItem.getQuantity());
        exist.setPrice(purchaseOrderItem.getPrice());
        exist.setPurchaseOrder(purchaseOrderItem.getPurchaseOrder());
        exist.setProduct(purchaseOrderItem.getProduct());
        exist.setReceivedQuantity(purchaseOrderItem.getReceivedQuantity());

        return purchaseOrderItemRepository.save(exist);
    }
}
