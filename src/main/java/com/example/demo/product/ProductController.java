package com.example.demo.product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    ProductService productService;
    public  ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 No Content if no products exist
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        logger.info("fetching product with ID:{} ", id);
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> {
            logger.warn("fetching product with ID:{} not found ", id);
            return ResponseEntity.notFound().build();
        });

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        if(productService.findById(id).isEmpty()) {
            logger.error("fetching product with ID:{} not found ", id);
            return ResponseEntity.badRequest().build();
        }
        productService.deleteById(id);
        return ResponseEntity.noContent().build();

    }
    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product product) {
        
        return ResponseEntity.ok(productService.save(product));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
if(productService.findById(id).isEmpty()) {
        logger.error("no product  exists with ID:{} ", id);
        return ResponseEntity.badRequest().build();

}
Product existingProduct = productService.findById(id).get();
existingProduct.setName(product.getName());
existingProduct.setDescription(product.getDescription());
return ResponseEntity.ok(productService.save(existingProduct));

    }





}
