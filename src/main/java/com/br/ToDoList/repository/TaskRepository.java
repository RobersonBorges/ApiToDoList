package com.br.ToDoList.repository;

import com.br.ToDoList.models.Justification;
import com.br.ToDoList.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Justification> findByCodTask(Long codTask);

    }

