package com.br.ToDoList.controller;

import com.br.ToDoList.dto.TaskDTO;
import com.br.ToDoList.enums.EnumTaskStatus;
import com.br.ToDoList.exceptions.ConfigDataResourceNotFoundException;
import com.br.ToDoList.models.Task;
import com.br.ToDoList.repository.TaskRepository;
import com.br.ToDoList.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping public List<TaskDTO> getTaskList() { List<Task> tasks = taskService.getAllTasks(); return tasks.stream().map(taskService::convertToDTO).collect(Collectors.toList()); }

    @GetMapping("/{codTask}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long codTask) {
        TaskDTO taskDTO = taskService.getTaskById(codTask);
        return ResponseEntity.ok(taskDTO);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO savedTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @PutMapping("/{codTask}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long codTask, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(codTask, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{codTask}")
    public ResponseEntity<?> deleteTask(@PathVariable Long codTask) {
        taskService.deleteTask(codTask);
        return ResponseEntity.noContent().build();
    }
}




