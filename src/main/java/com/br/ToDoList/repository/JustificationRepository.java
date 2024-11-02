package com.br.ToDoList.repository;

import com.br.ToDoList.models.Justification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JustificationRepository extends JpaRepository<Justification, Long> {

}
