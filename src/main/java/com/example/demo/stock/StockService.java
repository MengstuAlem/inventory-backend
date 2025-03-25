package com.example.demo.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    public List<Stock> findAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> findStockById(long id) {
        if (id<=0){
            throw new IllegalArgumentException("Stock id must be greater than 0");
        }

        return stockRepository.findById(id);
    }

    public Stock save(Stock stock) {
        if (stock==null){
            throw new IllegalArgumentException("Stock cannot be null");
        }
        if (stockRepository.findById(stock.getId()).isPresent()) {
            throw new IllegalArgumentException("Stock already exists");
        }
        return stockRepository.save(stock);
    }

    public void delete(long id) {
        if (stockRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Stock does not exist");
        }
        if (id<=0){
            throw new IllegalArgumentException("Stock id must be positive");
        }
        try {
            stockRepository.deleteById(id);
        } catch (RuntimeException e) {
                throw new RuntimeException(e);
        }


    }
    public Stock update(Long id ,Stock stock) {
        if (id<=0){
            throw new IllegalArgumentException("Stock id must be greater than 0");
        }
        if (stock==null){
            throw new IllegalArgumentException("Stock cannot be null");
        }
        if (stockRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Stock does not exist");
        }
        Stock existingStock = stockRepository.findById(id).get();
        existingStock.setType(stock.getType());
        existingStock.setQuantity(stock.getQuantity());
        existingStock.setReference(stock.getReference());

        return stockRepository.save(existingStock);
    }
}
