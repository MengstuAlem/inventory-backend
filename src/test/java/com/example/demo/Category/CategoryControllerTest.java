package com.example.demo.Category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @MockBean
    private CategoryService categoryService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getCategoryByExistIdTest() throws Exception {
        Category category = new Category(1L, "oil", "Best oil libya", null);
        when(categoryService.findById(1L)).thenReturn(Optional.of(category));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", 1L) // Use 1L instead of 1l
                        .contentType(MediaType.APPLICATION_JSON)) // No need for .content() in GET request
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("oil"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Best oil libya"));
    }

    @Test
    void getCategoryByInvalidIdTest() throws Exception {
        when(categoryService.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createCategoryTest() throws Exception {
        Category  category = new Category(1L, "oil", "Best oil libya", null);
        when(categoryService.save(category)).thenReturn(category);
        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(category)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("oil"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Best oil libya"));
    }

    @Test
    void deleteCategoryByValidIdTest() throws Exception {
        doNothing().when(categoryService).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
     @Test
    void deleteCategoryByInvalidIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", 0L))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
}
    @Test
    void deleteByNullIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", 0L))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateCategoryTest() throws Exception {
        Long id = 1L;
        Category category = new Category(id, "oil", "Best oil libya", null);
        Category updatesCategort= new Category(id,"oilUpdates","this is updated oil dis",null);
         when(categoryService.findById(id)).thenReturn(Optional.of(category));
        when(categoryService.update(1l,updatesCategort)).thenReturn(updatesCategort);
        mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatesCategort)))
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("oilUpdates"));


    }
    @Test
    void updateCategoryInvalidIdTest() throws Exception {
        Category category = new Category(1L, "oil", "Best oil libya", null);
        mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", 0L))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
    @Test
    void getAllCategoriesTest() throws Exception {
        Category category = new Category(1L, "oil", "Best oil libya", null);
        Category category2 = new Category(1L, "oil2", "Best oil libya", null);
        when(categoryService.findAll()).thenReturn(List.of(category,category2));
        mockMvc.perform(MockMvcRequestBuilders.get("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(List.of(category,category2))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));

    }



}