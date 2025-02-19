package com.todolist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todolist.exception.AccessDeniedException;
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
                .map(task -> new TaskDTO(task.getUserTaskId(), task.getTitle(), task.getDescription(), task.getCompleted()))
                .collect(Collectors.toList());
    }
    
    public TaskDTO getTaskById(Long id, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Garante que a tarefa pertence ao usuário atual
        if (!task.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access Denied: You do not have permission to access this task.");     
        }        
        return new TaskDTO(task.getUserTaskId(), task.getTitle(), task.getDescription(), task.getCompleted());
    }

    public TaskDTO saveTask(Task task, Long userId) {
        task.setUser(new User(userId)); // Define o usuário da tarefa

        // Calcula o próximo userTaskId para o usuário atual
        Long nextUserTaskId = taskRepository.findByUserId(userId).stream()
                .mapToLong(Task::getUserTaskId)
                .max().orElse(0) + 1;
        task.setUserTaskId(nextUserTaskId);

        Task savedTask = taskRepository.save(task);
        return new TaskDTO(savedTask.getUserTaskId(), savedTask.getTitle(), savedTask.getDescription(), savedTask.getCompleted());
    }

    public TaskDTO updateTask(Long id, Task taskDetails, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        // Garante que a tarefa pertence ao usuário atual
        if (!task.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access Denied: You do not have permission to access this task.");
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
        return new TaskDTO(updatedTask.getUserTaskId(), updatedTask.getTitle(), updatedTask.getDescription(), updatedTask.getCompleted());
    }

    public void deleteTask(Long id, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        if (!task.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access Denied: You do not have permission to access this task.");        
        }

        taskRepository.delete(task);
    }
}
