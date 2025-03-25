package com.example.demo.Category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAllCategoryTest() {
        List<Category>  result= List.of(new Category(1L,"oil","this best oil",Instant.now()),
                new Category(2L,"water","this best water", Instant.now()));
        when(categoryRepository.findAll()).thenReturn(result);
       assertEquals(categoryService.findAll(),result);
       assertEquals(categoryService.findAll().get(0).getName(),result.get(0).getName());

    }
    @Test
    void findCategoryByIdTest() {
        Category category = new Category(1L,"oil","this best oil",Instant.now());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        assertEquals(categoryService.findById(1l).get().getName(),category.getName());
    }
    @Test
    void findCategoryByInvalidTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.findById(0L);
        });
    }
    @Test
    void findCategoryByCategoryNameTest() {
        Category category = new Category(1L,"oil","this best oil",Instant.now());
        when(categoryRepository.findCategoryByName("oil")).thenReturn(Optional.of(category));
        assertEquals(categoryService.findCategoryByName("oil").get().getName(),category.getName());
        verify(categoryRepository,times(1)).findCategoryByName("oil");
    }

    @Test
    void findCategoryByCategoryNullNameTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.findCategoryByName(null);
        });
    }

    @Test
    void saveCategoryTest() {
        Category category = new Category(1L,"oil","this best oil",Instant.now());
        when(categoryRepository.save(category)).thenReturn(category);
        assertEquals(categoryService.save(category),category);
        verify(categoryRepository,times(1)).save(category);
    }
    @Test
    void saveNullCategoryTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.save(null);
        });
    }

    @Test
    void saveInvalidCategoryNameTest() {
        Category category = new Category(1L,null,"this best water",Instant.now());
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.save(category);
        });
    }
    @Test
    void deleteExistedCategoryTest() {
        when(categoryRepository.existsById(1l)).thenReturn(true);
        categoryService.deleteById(1l);
        verify(this.categoryRepository,times(1)).deleteById(1l);

    }
    @Test
    void deleteNoCategoryExistedTest() {
        when(categoryRepository.existsById(1l)).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryService.deleteById(1l);
        });
        String expectedMessage = "Category not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(this.categoryRepository,never()).deleteById(1l);
    }

    @Test
    void deleteCategoryInvalidIdTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.deleteById(0l);
        });
    }
    @Test
    void updateCategoryTest() {

        Long categoryId = 1L;
        Category existingCategory = new Category(categoryId, "oil", "this is the best oil", Instant.now());
        Category updatedCategoryData = new Category(categoryId, "water", "this is the best water", Instant.now());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        Category result = categoryService.update(categoryId, updatedCategoryData);

        assertNotNull(result);
        assertEquals(updatedCategoryData.getName(), result.getName());
        assertEquals(updatedCategoryData.getDescription(), result.getDescription());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void updateCategoryInvalidIdTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.update(0l, null);
        });
    }
    @Test
    void updateCategoryNullCategoryIdTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.update(1l, null);
        });
    }


}