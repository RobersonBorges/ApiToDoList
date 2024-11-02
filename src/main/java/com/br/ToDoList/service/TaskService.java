package com.br.ToDoList.service;

import com.br.ToDoList.dto.JustificationDTO;
import com.br.ToDoList.dto.TaskDTO;
import com.br.ToDoList.enums.EnumTaskStatus;
import com.br.ToDoList.exceptions.ConfigDataResourceNotFoundException;
import com.br.ToDoList.models.Category;
import com.br.ToDoList.models.Justification;
import com.br.ToDoList.models.Task;
import com.br.ToDoList.repository.CategoryRepository;
import com.br.ToDoList.repository.JustificationRepository;
import com.br.ToDoList.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired private JustificationRepository justificationRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public TaskDTO getTaskById(Long codTask) {
        Task task = taskRepository.findById(codTask).orElseThrow(() -> new ConfigDataResourceNotFoundException("Task not found"));
        return convertToDTO(task);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    public TaskDTO updateTask(Long codTask, TaskDTO taskDTO) {

        Task task = taskRepository.findById(codTask).orElseThrow(() -> new ConfigDataResourceNotFoundException("Task not found"));

        BeanUtils.copyProperties(taskDTO, task, "codTask", "categories", "justifications");

        preencherLstCategories(taskDTO, task);

        processPendingStatus(taskDTO, task);

        Task updatedTask = taskRepository.save(task);

        return convertToDTO(updatedTask);
    }

    public void deleteTask(Long codTask) {
        Task task = taskRepository.findById(codTask).orElseThrow(() -> new ConfigDataResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

    private void preencherLstCategories(TaskDTO taskDTO, Task task) {
        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(taskDTO.getCategoryIds()))) {
            Set<Category> categories = new HashSet<>();
            for (Long categoryId : taskDTO.getCategoryIds()) {
                Category categoryEntity = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ConfigDataResourceNotFoundException("Categoria não encontrada"));
                categories.add(categoryEntity);
            }
            task.setCategories(categories);
        }
    }

    private void processPendingStatus(TaskDTO taskDTO, Task task) {
        if (taskDTO.getEnumStatus() == EnumTaskStatus.PENDING) {
            if (taskDTO.getJustifications() != null && !taskDTO.getJustifications().isEmpty()) {
                for (JustificationDTO justificationDTO : taskDTO.getJustifications()) {
                    Justification justification = new Justification();
                    justification.setDescription(justificationDTO.getDescription());
                    justification.setTask(task);
                    task.getJustifications().add(justification);
                }
            } else {
                throw new IllegalArgumentException("Justification is required when status changes to PENDING");
            }
        }
    }



    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setCodTask(task.getCodTask());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setEnumStatus(task.getEnumStatus());

        Set<JustificationDTO> justificationDTOs = new HashSet<>();
        for (Justification justification : task.getJustifications()) {
            JustificationDTO justificationDTO = new JustificationDTO();
            justificationDTO.setCodJustify(justification.getCodJustify());
            justificationDTO.setDescription(justification.getDescription());
            justificationDTO.setCodTask(justification.getTask().getCodTask());
            justificationDTOs.add(justificationDTO);
        }
        taskDTO.setJustifications(justificationDTOs);

        return taskDTO;
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setEnumStatus(taskDTO.getEnumStatus());
        preencherLstCategories(taskDTO, task);
        if (taskDTO.getEnumStatus() == EnumTaskStatus.PENDING && taskDTO.getJustifications() != null) {
            Set<Justification> justifications = new HashSet<>();
            for (JustificationDTO justificationDTO : taskDTO.getJustifications()) {
                Justification justification = new Justification();
                justification.setDescription(justificationDTO.getDescription());
                justification.setTask(task);
                justifications.add(justification);
            }
            task.setJustifications(justifications);
        }
        return task;
    }
}
