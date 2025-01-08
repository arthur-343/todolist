package com.todolist.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todolist.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    Long countByUserId(Long userId);
    Optional<Task> findByUserTaskIdAndUserId(Long userTaskId, Long userId); // Novo método
}
