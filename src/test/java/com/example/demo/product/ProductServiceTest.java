package com.example.demo.product;

import com.example.demo.Category.Category;
import com.example.demo.supplier.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.example.demo.product.ProductStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllProducts() {
        List<Product> result =List.of(
                new Product(1l,"water","best water",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier()),
                new Product(2l,"water2","best water2",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier())
        );
      when(productRepository.findAll()).thenReturn(result);
      assertEquals(2,productService.findAll().size());
      assertEquals(result.get(0).getName(),productService.findAll().get(0).getName());

    }

    @Test
    void getProductByValidId() {
        Product product =new Product(1l,"water","best water",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier());
        when(productRepository.findById(1l)).thenReturn(Optional.of(product));
        assertEquals(productService.findById(1l).get().getName(),product.getName());
    }
    @Test
    void getProductByInvalidId() {
        when(productRepository.findById(1l)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> productService.findById(0l));
    }

    @Test
    void saveValidProduct() {
        Product product =new Product(1l,"water","best water",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier());
        when(productRepository.save(product)).thenReturn(product);
        assertEquals(product,productService.save(product));
    }
    @Test
    void saveInvalidProduct() {
       assertThrows(IllegalArgumentException.class,()->{
           productService.save(null);
       });
    }

    @Test
    void deleteProductByExistedId() {
        when(productRepository.existsById(1l)).thenReturn(true);
        productService.deleteById(1l);
        verify(productRepository,times(1)).deleteById(1l);
    }
    @Test
    void deleteProductByInvalidId() {
        assertThrows(IllegalArgumentException.class,()->{
            productService.deleteById(0l);
        });
        verify(productRepository,never()).deleteById(1l);
    }

    @Test
    void deleteNoExistProductById() {
        when(productRepository.existsById(1l)).thenReturn(false);
        assertThrows(IllegalArgumentException.class,()->{
            productService.deleteById(1l);
        });
        verify(productRepository,never()).deleteById(1l);
    }

    @Test
    void updateProduct() {
        // Given: existing product and new update product
        Product existproduct = new Product(1L, "water", "best water", new BigDecimal(1233), new BigDecimal(234), 100, 5, ACTIVE, Instant.now(), null, new Category(), new Supplier());
        Product updateProduct = new Product(1L, "water2", "best water2", new BigDecimal(1233), new BigDecimal(234), 100, 5, ACTIVE, Instant.now(), null, new Category(), new Supplier());

        when(productRepository.findById(1L)).thenReturn(Optional.of(existproduct));
        when(productRepository.save(any(Product.class))).thenReturn(existproduct); // Ensure mock saves updated product

        // When: updating the product
        Product result = productService.update(1L, updateProduct);

        // Then: validate changes
        assertNotNull(result);
        assertEquals("water2", result.getName());  // Ensure name is updated
        assertEquals("best water2", result.getDescription());  // Ensure description is updated

        verify(productRepository).save(any(Product.class)); // Ensure save is called
    }


    @Test
    void updateInvalidProduct() {
        assertThrows(IllegalArgumentException.class,()->{
            productService.update(1l,null);
        });
        verify(productRepository,never()).deleteById(1l);
    }

    @Test
    void updateNoExistProductById() {
        when(productRepository.findById(1l)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,()->{
            productService.update(1l,new Product());
        });
        verify(productRepository,never()).deleteById(1l);
    }





}