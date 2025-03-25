package com.example.demo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
//@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    // Find all categories
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Find category by ID
    public Optional<Category> findById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number.");
        }
        return categoryRepository.findById(id);
    }

    // Find category by name
    public Optional<Category> findCategoryByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        return categoryRepository.findCategoryByName(name);
    }

    // Save a new category

    public Category save(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        return categoryRepository.save(category);
    }

    // Delete category by ID

    public void deleteById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number.");
        }
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found.");
        }
        categoryRepository.deleteById(id);
    }

    // Update an existing category

    public Category update(Long id, Category category) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }

        // Find the existing category or throw an exception if not found
        Category existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category with ID " + id + " not found."));

        // Update fields only if they are not null
        if (category.getName() != null) {
            existCategory.setName(category.getName());
        }
        if (category.getDescription() != null) {
            existCategory.setDescription(category.getDescription());
        }

        return categoryRepository.save(existCategory);
    }
}