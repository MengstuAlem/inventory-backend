package com.example.demo.saleitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository saleItemRepository;
    public List<SaleItem> findAll() {

        return saleItemRepository.findAll();
    }

    public SaleItem save(SaleItem saleItem) {
        if (saleItem==null) {
             throw  new IllegalArgumentException("saleItem is null");
        }
        return saleItemRepository.save(saleItem);
    }

    public void delete(long id) {
        if (!saleItemRepository.existsById(id)) {
            throw  new IllegalArgumentException("saleItem not found");
        }
        saleItemRepository.deleteById(id);

    }

    public SaleItem update(long id, SaleItem saleItem) {
        if (!saleItemRepository.existsById(id) || saleItem==null) {
            throw  new IllegalArgumentException("saleItem not found");
        }
        SaleItem saleItemSaved = saleItemRepository.save(saleItem);
        saleItemSaved.setPrice(saleItem.getPrice());
        saleItemSaved.setQuantity(saleItem.getQuantity());
        return saleItemRepository.save(saleItemSaved);
    }

    public Optional<SaleItem> findById(long id) {
        return saleItemRepository.findById(id);
    }
}
