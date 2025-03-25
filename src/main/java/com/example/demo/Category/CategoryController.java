package com.example.demo.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("categories")
@CrossOrigin("http://localhost:4200")
public class CategoryController {
    private static final Logger logger =  LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable long id) {

        logger.info("Fetching category with ID: {}", id);

        Optional<Category> category = categoryService.findById(id);

        return category.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Category with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        logger.info("Creating category: {}", category);
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.ok().body(savedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        logger.info("Deleting category with ID: {}", id);

        // Validate the ID
        if (id == null || id <= 0) {
            logger.warn("Invalid category ID: {}", id);
            return ResponseEntity.badRequest().build();
        }

        // Delete the category
        try {
            categoryService.deleteById(id);
            logger.info("Category with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete category with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id,@RequestBody Category category) {
        logger.info("Updating category: {}", category);
        if (id == null || id <= 0) {
            logger.warn("Invalid category ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(categoryService.update(id,category));
    }
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        logger.info("Fetching all categories");
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok().body(categories);
    }
}
