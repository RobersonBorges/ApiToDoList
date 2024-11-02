package com.br.ToDoList.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Justification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codJustify;

    private String description;

    @ManyToOne
    @JoinColumn(name = "codTask")
    private Task task;

}
