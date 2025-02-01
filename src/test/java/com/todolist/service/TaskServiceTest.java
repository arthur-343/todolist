package com.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.todolist.exception.TaskNotFoundException;
import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.model.dto.TaskDTO;
import com.todolist.repositories.TaskRepository;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        task1 = new Task();
        task1.setId(1L);
        task1.setUser(user);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setCompleted(false);
        task1.setUserTaskId(1L);

        task2 = new Task();
        task2.setId(2L);
        task2.setUser(user);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setCompleted(false);
        task2.setUserTaskId(2L);
    }

    @Test
    void testGetTasksByUser() {
        when(taskRepository.findByUserId(1L)).thenReturn(Arrays.asList(task1, task2));

        List<TaskDTO> taskDTOs = taskService.getTasksByUser(1L);

        assertNotNull(taskDTOs);
        assertEquals(2, taskDTOs.size());
        assertEquals(task1.getId(), taskDTOs.get(0).getId());
        assertEquals(task2.getId(), taskDTOs.get(1).getId());
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        TaskDTO taskDTO = taskService.getTaskById(1L, 1L);

        assertNotNull(taskDTO);
        assertEquals(task1.getId(), taskDTO.getId());

        // Testa exceção para tarefa não encontrada
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(3L, 1L);
        });

        // Testa exceção para acesso negado
        task1.getUser().setId(2L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        assertThrows(RuntimeException.class, () -> {
            taskService.getTaskById(1L, 1L);
        });
    }

    @Test
    void testSaveTask() {
        when(taskRepository.findByUserId(1L)).thenReturn(Arrays.asList(task1, task2));
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("New Description");

        TaskDTO savedTaskDTO = taskService.saveTask(task, 1L);

        assertNotNull(savedTaskDTO);
        assertEquals(task1.getId(), savedTaskDTO.getId());
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Task");

        TaskDTO updatedTaskDTO = taskService.updateTask(1L, taskDetails, 1L);

        assertNotNull(updatedTaskDTO);
        assertEquals("Updated Task", updatedTaskDTO.getTitle());

        // Testa exceção para tarefa não encontrada
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(3L, taskDetails, 1L);
        });

        // Testa exceção para acesso negado
        task1.getUser().setId(2L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(1L, taskDetails, 1L);
        });
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        taskService.deleteTask(1L, 1L);

        verify(taskRepository, times(1)).delete(task1);

        // Testa exceção para tarefa não encontrada
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(3L, 1L);
        });

        // Testa exceção para acesso negado
        task1.getUser().setId(2L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(1L, 1L);
        });
    }
}
