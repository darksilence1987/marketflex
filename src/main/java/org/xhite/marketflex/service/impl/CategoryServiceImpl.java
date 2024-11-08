package org.xhite.marketflex.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhite.marketflex.dto.CategoryDto;
import org.xhite.marketflex.dto.CreateCategoryRequest;
import org.xhite.marketflex.exception.ResourceNotFoundException;
import org.xhite.marketflex.model.Category;
import org.xhite.marketflex.repository.CategoryRepository;
import org.xhite.marketflex.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final org.xhite.marketflex.mapper.CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(key = "'all'")
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }
    ;
    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public CategoryDto createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public CategoryDto updateCategory(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getFeaturedCategories(int limit) {
        List<Category> categories = categoryRepository.findAll(PageRequest.of(0, limit)).getContent();
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

}