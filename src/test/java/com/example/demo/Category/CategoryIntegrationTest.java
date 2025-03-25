package com.example.demo.Category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional // Ensures database cleanup after each test
public class CategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryService categoryService; // Use real DB

    @Test
    @Order(1)
    void createCategory() throws Exception {
        Category category = new Category();
        category.setName("test");
        category.setDescription("this is test dis");
        category.setCreated(Instant.now());
        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("this is test dis"));
    }

    @Test
    @Order(2)
    void getCategoryById() throws Exception {
        // ✅ Save category to real database
        Category category = new Category(null, "test", "this is test dis", Instant.now());
        category = categoryService.save(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", category.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
    }

    @Test
    void updateCategory() throws Exception {
        Category category = new Category(null, "test1", "this is test dis 2", Instant.now());
        category = categoryService.save(category);
        mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());

    }

    @Test
    @Order(4)
    void deleteCategoryById() throws Exception {
        // ✅ Save category to real database
        Category category = new Category(null, "test", "this is test dis", Instant.now());
        category = categoryService.save(category);

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", category.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
