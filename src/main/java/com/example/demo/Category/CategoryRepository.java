package com.example.demo.Category;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
//@Transactional
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findCategoryByName(String name);


}
