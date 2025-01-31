package com.zosh.controller;

import com.zosh.dto.SalonDTO;
import com.zosh.modal.Category;
import com.zosh.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/salon-owner") // owner specific end point
public class SalonCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {

        // for the time being we are creating static ssalonDTO,but once we will integrate keycock then
        // it will be passthrough JWT token

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);

        Category savedCategory = categoryService.saveCategory(category, salonDTO);
        return ResponseEntity.ok(savedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) throws Exception {

        // for the time being we are creating static ssalonDTO,but once we will integrate keycock then
        // it will be passthrough JWT token
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);

        categoryService.deleteCategoryById(id, salonDTO.getId());

        return ResponseEntity.ok("Category deleted successfully");
    }
}
