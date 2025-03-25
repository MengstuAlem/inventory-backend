package com.example.demo.sale;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }
    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Sale save(Sale sale) {
        if (sale == null) {
            throw new IllegalArgumentException("sale is null");
        }

        return saleRepository.save(sale);
    }

    public Optional<Sale> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        return saleRepository.findById(id);
    }

    public void deleteById(long id) {
        if (findById(id).isEmpty()) {
            throw new EntityNotFoundException("Sale not found with id " + id);
        }
        saleRepository.deleteById(id);
    }

    public Sale update(long id,Sale sale) {
        Sale existingSale =  saleRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Sale with id: " + sale.getId() + " not found"));
        if (sale.getCustomerName() != null) {
            existingSale.setCustomerName(sale.getCustomerName());
        }
        if (sale.getTotalAmount() != null) {
            existingSale.setTotalAmount(sale.getTotalAmount());
        }
        if (sale.getStatus() != null) {
            existingSale.setStatus(sale.getStatus());
        }
        return saleRepository.save(existingSale);
    }
}
