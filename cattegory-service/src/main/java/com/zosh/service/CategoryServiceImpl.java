package com.zosh.service;

import com.zosh.dto.SalonDTO;
import com.zosh.modal.Category;
import com.zosh.repository.CategoryRepository;
import com.zosh.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    // instance of CategoryRepository
    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category, SalonDTO salonDTO) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setImage(category.getImage());
        newCategory.setSalonId(salonDTO.getId());
        return categoryRepository.save(newCategory);
    }

    @Override
    public Set<Category> getAllCategoryBySalon(Long id) {
        return categoryRepository.findBySalonId(id);
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {

        Category category = categoryRepository.findById(id).orElse(null);

        if( category == null){
            throw new Exception("Category id not found");
        }

        return category;
    }

    @Override
    public void deleteCategoryById(Long id,Long salonId) throws Exception {
        Category category = getCategoryById(id);

        // doubt hai yaha
        if((category.getId().equals(salonId))){
            throw new Exception("you don't have permission to delete this category");
        }
        categoryRepository.deleteById(id);

    }
}
