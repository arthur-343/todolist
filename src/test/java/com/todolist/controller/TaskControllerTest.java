package com.todolist.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.todolist.exception.AccessDeniedException;
import com.todolist.exception.TaskNotFoundException;
import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.model.dto.TaskDTO;
import com.todolist.service.TaskService;

public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private Principal principal;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
    }

    @Test
    public void testGetTasks() {
        List<TaskDTO> taskList = Arrays.asList(new TaskDTO(), new TaskDTO());
        when(taskService.getTasksByUser(1L)).thenReturn(taskList);

        ResponseEntity<List<TaskDTO>> response = taskController.getTasks(principal);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().size() == 2;
    }

    @Test
    public void testGetTasksException() {
        when(taskService.getTasksByUser(1L)).thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<List<TaskDTO>> response = taskController.getTasks(principal);

        assert response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
        assert response.getBody() == null;
    }

    @Test
    public void testGetTaskById() {
        TaskDTO task = new TaskDTO();
        when(taskService.getTaskById(anyLong(), anyLong())).thenReturn(task);

        ResponseEntity<TaskDTO> response = taskController.getTaskById(1L, principal);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
    }

    @Test
    public void testGetTaskById_TaskNotFoundException() {
        when(taskService.getTaskById(anyLong(), anyLong())).thenThrow(new TaskNotFoundException(1L));

        ResponseEntity<TaskDTO> response = taskController.getTaskById(1L, principal);

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        assert response.getBody() == null;
    }

    @Test
    public void testGetTaskById_AccessDeniedException() {
        when(taskService.getTaskById(anyLong(), anyLong())).thenThrow(new AccessDeniedException("Acesso negado"));

        ResponseEntity<TaskDTO> response = taskController.getTaskById(1L, principal);

        assert response.getStatusCode() == HttpStatus.FORBIDDEN;
        assert response.getBody() == null;
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.saveTask(any(Task.class), anyLong())).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = taskController.createTask(task, principal);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
    }

    @Test
    public void testCreateTask_Exception() {
        Task task = new Task();
        when(taskService.saveTask(any(Task.class), anyLong())).thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<TaskDTO> response = taskController.createTask(task, principal);

        assert response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
        assert response.getBody() == null;
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task();
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.updateTask(anyLong(), any(Task.class), anyLong())).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = taskController.updateTask(1L, task, principal);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
    }

    @Test
    public void testUpdateTask_TaskNotFoundException() {
        Task task = new Task();
        when(taskService.updateTask(anyLong(), any(Task.class), anyLong())).thenThrow(new TaskNotFoundException(1L));

        ResponseEntity<TaskDTO> response = taskController.updateTask(1L, task, principal);

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        assert response.getBody() == null;
    }

    @Test
    public void testUpdateTask_AccessDeniedException() {
        Task task = new Task();
        when(taskService.updateTask(anyLong(), any(Task.class), anyLong())).thenThrow(new AccessDeniedException("Acesso negado"));

        ResponseEntity<TaskDTO> response = taskController.updateTask(1L, task, principal);

        assert response.getStatusCode() == HttpStatus.FORBIDDEN;
        assert response.getBody() == null;
    }

    @Test
    public void testDeleteTask() {
        ResponseEntity<Void> response = taskController.deleteTask(1L, principal);

        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    @Test
    public void testDeleteTask_TaskNotFoundException() {
        doThrow(new TaskNotFoundException(1L)).when(taskService).deleteTask(anyLong(), anyLong());

        ResponseEntity<Void> response = taskController.deleteTask(1L, principal);

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void testDeleteTask_AccessDeniedException() {
        doThrow(new AccessDeniedException("Acesso negado")).when(taskService).deleteTask(anyLong(), anyLong());

        ResponseEntity<Void> response = taskController.deleteTask(1L, principal);

        assert response.getStatusCode() == HttpStatus.FORBIDDEN;
    }
}
