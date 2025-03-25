package com.example.demo.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(long id) {
      if ( id<=0){
          throw new IllegalArgumentException("id must be positive");
      }
        return  productRepository.findById(id);
    }

    public Product save(Product product) {
        if (product==null){
            throw new IllegalArgumentException("product must not be null");
        }
        return productRepository.save(product);
    }

    public void deleteById(long id) {
        if (id<=0){
            throw new IllegalArgumentException("id must be positive");
        }
        if(!productRepository.existsById(id)){
            throw new IllegalArgumentException("product not found");
        }
     productRepository.deleteById(id);
    }

    public Product update(long id, Product updateProduct) {
        if (updateProduct == null) {
            throw new IllegalArgumentException("Product must not be null");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Update only non-null values (optional)
        product.setName(updateProduct.getName());
        product.setDescription(updateProduct.getDescription());
        if (updateProduct.getPrice() != null) {
            product.setPrice(updateProduct.getPrice());
        }
        if (updateProduct.getStockQuantity() > 0) {
            product.setStockQuantity(updateProduct.getStockQuantity());
        }
        if (updateProduct.getCategory() != null) {
            product.setCategory(updateProduct.getCategory());
        }
        if (updateProduct.getSupplier() != null) {
            product.setSupplier(updateProduct.getSupplier());
        }

        return productRepository.save(product);
    }

}
