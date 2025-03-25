package com.example.demo.supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("suppliers")
@CrossOrigin(origins = "http://localhost:4200")
public class SupplierController {
    private final SupplierService supplierService;
    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getSuppliers() {
        logger.info("fetching  all suppliers");
        return ResponseEntity.ok(supplierService.findAll());
    }

    @PostMapping
    public ResponseEntity<Supplier> saveSuppliers(@RequestBody Supplier supplier) {

        if (supplier == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(supplierService.save(supplier));

    }
    @GetMapping("/{id}")
    public ResponseEntity<Supplier>  getSupplierById(@PathVariable long id) {
        logger.info("fetching supplier:{}", id);
        if (id<=0) {
            logger.error("supplier id not found");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(supplierService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "supplier not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierById(@PathVariable long id) {
        logger.info("deleting supplier:{}", id);
        if (id<=0) {
            return ResponseEntity.badRequest().build();
        }
        try {
            supplierService.deleteById(id);
            logger.info("supplier with id:{} deleted successfully", id);
        }catch (ResponseStatusException e) {
            logger.info("supplier with id:{} delete failed", id,e);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return  ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplierById(@PathVariable long id, @RequestBody Supplier supplier) {
        logger.info("updating supplier:{}", supplier);
        if (id<=0 || supplier==null) {
            logger.warn("Invalid supplier id {}",id);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(supplierService.Update(id,supplier));
    }



}
