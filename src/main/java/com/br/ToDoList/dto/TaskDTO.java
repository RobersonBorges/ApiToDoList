package com.br.ToDoList.dto;

import com.br.ToDoList.enums.EnumTaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private Long codTask;
    private String title;
    private String description;
    private EnumTaskStatus enumStatus;
    private Set<Long> categoryIds;
    private Set<JustificationDTO> justifications;

}
