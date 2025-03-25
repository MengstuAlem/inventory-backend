package com.example.demo.stock;

import com.example.demo.Category.CategoryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stocks")
public class StockController {
    private static final Logger logger =  LoggerFactory.getLogger(StockController.class);
    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>>getAllStocks() {
        logger.info("get All Stocks");

        return ResponseEntity.ok(stockService.findAllStocks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        logger.info("get Stock by id {}", id);
        if (stockService.findStockById(id).isPresent()) {
            return ResponseEntity.ok(stockService.findStockById(id).get());
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        logger.info("create Stock {}", stock);
        if (stockService.findStockById(stock.getId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(stockService.save(stock));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockById(@PathVariable Long id) {
        logger.info("delete Stock {}", id);
        if (stockService.findStockById(id).isEmpty()) {
            logger.info("delete Stock id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("delete Stock id {}", id);
        stockService.delete(id);
        return ResponseEntity.noContent().build();

    }
    @PutMapping
    public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) {
        logger.info("update Stock {}", stock);
        if (stockService.findStockById(stock.getId()).isPresent()) {
            return ResponseEntity.ok(stockService.save(stock));
        }
        return ResponseEntity.notFound().build();
    }
}
