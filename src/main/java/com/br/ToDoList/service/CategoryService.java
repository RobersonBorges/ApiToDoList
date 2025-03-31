package com.br.ToDoList.service;

import com.br.ToDoList.exceptions.ConfigDataResourceNotFoundException;
import com.br.ToDoList.models.Category;
import com.br.ToDoList.models.Task;
import com.br.ToDoList.repository.CategoryRepository;
import com.br.ToDoList.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TaskRepository taskRepository;


    public ResponseEntity<?> createCategory(Category category) {
        validateCategory(category);
        Category newCategory = categoryRepository.save(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ConfigDataResourceNotFoundException("Categoria não encontrada"));
        validateUpdateCategory(categoryDetails, category );
        category.setNameCategory(categoryDetails.getNameCategory());
        Category updatedCategory = categoryRepository.save(category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ConfigDataResourceNotFoundException("Categoria não encontrada"));
        validateDeletetionCategory(category);
        categoryRepository.delete(category);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void validateCategory(Category category){
        if(category.getNameCategory() == null){
            throw new IllegalArgumentException("Category name is required.");
        }
        if (categoryRepository.existsByNameCategory(category.getNameCategory())) {
            throw new IllegalArgumentException("Category already exist.");
        }
    }

    private void validateUpdateCategory(Category categoryNew, Category categoryBD){
        if(categoryNew.getNameCategory() == null){
            throw new IllegalArgumentException("Category name is required.");
        }
        if ( Boolean.FALSE.equals( categoryNew.getNameCategory().equals(categoryBD.getNameCategory()))){
            if (categoryRepository.existsByNameCategory(categoryNew.getNameCategory())) {
                throw new IllegalArgumentException("Category already exist.");
            }
        }
    }



    private void validateDeletetionCategory(Category category) {
        List<Task> taskList = taskRepository.findByCategoriesCodCategory(category.getCodCategory());
        if (Boolean.FALSE.equals(taskList.isEmpty())) {
            throw new IllegalArgumentException("Cannot delete category because it is linked to existing tasks.");
        }
    }

}
