package com.example.demo.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("sales")
public class SaleController {

    public final SaleService saleService;
    public final Logger logger= LoggerFactory.getLogger(SaleController.class);
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<Sale>> findAll() {
        logger.info("fetching all sales");
        return ResponseEntity.ok(saleService.findAll());

    }
    @PostMapping
    public ResponseEntity<Sale>  save(@RequestBody Sale sale) {
        logger.info("saving sale {}" , sale);
        if (sale == null) {
            logger.warn("sale is null");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saleService.save(sale));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Sale> findById(@PathVariable long id) {
        logger.info("fetching sale:{}", id);
        if (id<=0) {
            logger.warn("sale:{} not found", id);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saleService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"sale not find")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
      logger.info("deleting sale:{}", id);
      if (id<=0) {
        logger.warn("sale:{} not found", id);
        return  ResponseEntity.badRequest().build();
      }
      try {
          saleService.deleteById(id);
          return ResponseEntity.noContent().build();
      }catch (ResponseStatusException e){
          logger.warn("sale with id :{} delete failed", id);
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> update(@PathVariable long id, @RequestBody Sale sale) {
        logger.info("updating sale:{}", sale);
        if (id<=0 || sale == null) {
            logger.warn("sale:{} not found", id);
            return  ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(saleService.update(id,sale));
    }

}
