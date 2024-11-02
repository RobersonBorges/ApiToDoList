package com.br.ToDoList.repository;

import com.br.ToDoList.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameCategory(String nameCategory);
}
