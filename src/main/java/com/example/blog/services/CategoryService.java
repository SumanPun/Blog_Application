package com.example.blog.services;

import com.example.blog.entites.Category;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.payloads.CategoryDto;
import com.example.blog.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category category = this.dtoToCategory(categoryDto);
        Category savedCategory = this.categoryRepository.save(category);
        return this.categoryToDto(savedCategory);

    }

    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));
        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        Category updatedCategory = this.categoryRepository.save(category);

        return this.categoryToDto(updatedCategory);
    }

    public void deleteCategory(Integer category_id) {
        Category category = this.categoryRepository.findById(category_id).orElseThrow(()-> new ResourceNotFoundException("category","category",category_id));
        this.categoryRepository.delete(category);
    }

    public CategoryDto getCategoryById(Integer category_id) {
        Category category = this.categoryRepository.findById(category_id).orElseThrow(()-> new ResourceNotFoundException("category","categoryId",category_id));
        return this.categoryToDto(category);
    }

    public List<CategoryDto> getAllCategory() {
        List<Category> categories = this.categoryRepository.findAll();
        List<CategoryDto> categories1 = categories.stream().map(category -> this.categoryToDto(category)).collect(Collectors.toList());
        return categories1;
    }

    private CategoryDto categoryToDto(Category category) {
        CategoryDto categoryDto = this.modelMapper.map(category,CategoryDto.class);
        return categoryDto;
    }
    private Category dtoToCategory(CategoryDto categoryDto) {
        Category category = this.modelMapper.map(categoryDto,Category.class);
        return category;
    }
}
