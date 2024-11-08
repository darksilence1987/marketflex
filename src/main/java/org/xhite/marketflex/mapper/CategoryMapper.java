package org.xhite.marketflex.mapper;

import org.springframework.stereotype.Component;
import org.xhite.marketflex.dto.CategoryDto;
import org.xhite.marketflex.model.Category;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        if (category == null) return null;

        return CategoryDto.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
    }
    
}
