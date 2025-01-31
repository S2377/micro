package com.zosh.controller;

import com.zosh.modal.Category;
import com.zosh.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/salon/{id}")
    public ResponseEntity<Set<Category>> getCategoriesBySalonId(@PathVariable Long id){

        Set<Category> categories = categoryService.getAllCategoryBySalon(id);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoriesById(@PathVariable Long id) throws Exception {

        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

}
