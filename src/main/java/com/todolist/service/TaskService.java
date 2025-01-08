package com.todolist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todolist.exception.TaskNotFoundException;
import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.model.dto.TaskDTO;
import com.todolist.repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<TaskDTO> getTasksByUser(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream()
                .map(task -> new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getCompleted()))
                .collect(Collectors.toList());
    }
    
    public TaskDTO getTaskById(Long id, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Garante que a tarefa pertence ao usuário atual
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access Denied");
        }
        
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getCompleted());
    }

    public TaskDTO saveTask(Task task, Long userId) {
        task.setUser(new User(userId)); // Define o usuário da tarefa
        Task savedTask = taskRepository.save(task);
        return new TaskDTO(savedTask.getId(), savedTask.getTitle(), savedTask.getDescription(), savedTask.getCompleted());
    }

    public TaskDTO updateTask(Long id, Task taskDetails, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        // Garante que a tarefa pertence ao usuário atual
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access Denied");
        }

        if (taskDetails.getTitle() != null) {
            task.setTitle(taskDetails.getTitle());
        }
        if (taskDetails.getCompleted() != null) {
            task.setCompleted(taskDetails.getCompleted());
        }
        if (taskDetails.getDescription() != null) {
            task.setDescription(taskDetails.getDescription());
        }
        Task updatedTask = taskRepository.save(task);
        return new TaskDTO(updatedTask.getId(), updatedTask.getTitle(), updatedTask.getDescription(), updatedTask.getCompleted());
    }

    public void deleteTask(Long id, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access Denied");
        }

        taskRepository.delete(task);
    }
}
