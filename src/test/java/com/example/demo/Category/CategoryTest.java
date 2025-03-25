package com.example.demo.Category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    Category category;
    @BeforeEach
    void setUp() {
         category = new Category(1l,"oil","best oil",null);
    }

    @Test
    void getNameTest() {
       assertEquals("oil",category.getName());
    }
    @Test
    void getIdTest() {
        assertEquals(1l,category.getId());
    }
    @Test
    void getDescriptionTest() {
        assertEquals("best oil",category.getDescription());
    }

}