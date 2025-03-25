package com.example.demo.saleitem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sale-items")
public class SaleItemController {
    private Logger logger= LoggerFactory.getLogger(SaleItemController.class);
    @Autowired
    private SaleItemService saleItemService;

    @GetMapping
    public ResponseEntity<List<SaleItem>> getSaleItems() {
        logger.info("getSaleItems");
        return ResponseEntity.ok(saleItemService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<SaleItem> getSaleItem(@PathVariable Long id){
      logger.info("getSaleItem");
       if(id<=0){
           logger.info("id  shoud be positive");
           return ResponseEntity.notFound().build();
       }
        return ResponseEntity.ok(saleItemService.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("SaleItem not found")
        ));
    }
    @PostMapping
    public ResponseEntity<SaleItem> saveSaleItem(@RequestBody SaleItem saleItem){
        logger.info("saveSaleItem");
        if(saleItem==null){
            logger.warn("saleItem is null");
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.ok(saleItemService.save(saleItem));
    }
  @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Long id){
        logger.info("delete SaleItem by id {}",id);
        if (id<=0) {
            logger.warn("id  should be positive");
            return ResponseEntity.badRequest().build();
        }
        try {
            saleItemService.delete(id);
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }

        return ResponseEntity.noContent().build()  ;
  }

  @PutMapping("/{id}")
    public ResponseEntity<SaleItem>  updateSaleItem(@PathVariable Long id, @RequestBody SaleItem saleItem){
        logger.info("update SaleItem");
        if(id<=0 || saleItem==null){
            logger.warn("saleItem is null");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saleItemService.save(saleItem));
  }

}
