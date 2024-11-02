package com.br.ToDoList.service;

import com.br.ToDoList.exceptions.ConfigDataResourceNotFoundException;
import com.br.ToDoList.models.Category;
import com.br.ToDoList.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<?> createCategory(Category category) {
        if (categoryRepository.existsByNameCategory(category.getNameCategory())) {
            return new ResponseEntity<>("Categoria já existe!", HttpStatus.BAD_REQUEST);
        }
        Category newCategory = categoryRepository.save(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateCategory(Long id, Category categoryDetails) {
        if (categoryRepository.existsByNameCategory(categoryDetails.getNameCategory())) {
            return new ResponseEntity<>("Categoria já existe!", HttpStatus.BAD_REQUEST);
        }
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ConfigDataResourceNotFoundException("Categoria não encontrada"));
        category.setNameCategory(categoryDetails.getNameCategory());
        Category updatedCategory = categoryRepository.save(category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ConfigDataResourceNotFoundException("Categoria não encontrada"));
        categoryRepository.delete(category);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
