package com.example.demo.product;

import com.example.demo.Category.Category;
import com.example.demo.supplier.Supplier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.example.demo.product.ProductStatus.ACTIVE;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @MockBean
   private ProductService productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllExistedProductTest() throws Exception {
        List<Product> result =List.of(
                new Product(1L,"water","best water",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier()),
                new Product(2L,"water2","best water2",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier())
        );

        when(productService.findAll()).thenReturn(result);
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("water"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("water2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));


    }
    @Test
    void getNonExistedProductTest() throws Exception {
        when(productService.findAll()).thenReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void getProductByValidIdTest() throws Exception {
        long id = 1L;
        Product product = new Product(id,"water","best water",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier());
        when(productService.findById(id)).thenReturn(Optional.of(product));
        mockMvc.perform(MockMvcRequestBuilders.get("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("water"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("best water"));

    }
    @Test
    void getProductByNoExistIdTest() throws Exception {
        when(productService.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteProductByExistIdTest() throws Exception {
        long id = 1L;
        Product product = new Product(id,"water","best water",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier());
        when(productService.findById(id)).thenReturn(Optional.of(product));
        doNothing().when(productService).deleteById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    void deleteProductInValidIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveValidProductTest() throws Exception {
        long id = 1L;
        Product product = new Product(id,"water","best water",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier());
        when(productService.findById(id)).thenReturn(Optional.empty());
        when(productService.save(product)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void saveProductInValidProductTest() throws Exception {
        long id = 1L;
        Product product = new Product(id,"water","best water",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier());
        when(productService.findById(id)).thenReturn(Optional.of(product));
        when(productService.save(product)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/products"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateValidProductTest() throws Exception {
        long id = 1L;
        Product product = new Product(id,"water","best water",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier());
        Product updatedProduct = new Product(id,"water2","best water2",new BigDecimal(1233),new BigDecimal(234),100,5,ACTIVE, Instant.now(),null ,new Category(),new Supplier());
        when(productService.findById(id)).thenReturn(Optional.of(product));
        when(productService.save(product)).thenReturn(updatedProduct);
        mockMvc.perform(MockMvcRequestBuilders.put("/products/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    void updateNonExistProductTest() throws Exception {
        long id = 1L;
        when(productService.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/products/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }




}