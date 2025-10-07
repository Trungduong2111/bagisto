package com.example.ecomm.service;

import com.example.ecomm.dto.CategoryDto;
import com.example.ecomm.entity.Category;
import com.example.ecomm.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Category not found"));
    }

    @Transactional
    public Category create(CategoryDto dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setSlug(dto.getSlug());
        c.setDescription(dto.getDescription());
        return categoryRepository.save(c);
    }

    @Transactional
    public Category update(Long id, CategoryDto dto) {
        Category c = getById(id);
        c.setName(dto.getName());
        c.setSlug(dto.getSlug());
        c.setDescription(dto.getDescription());
        return categoryRepository.save(c);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
