package com.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.todolist.exception.AccessDeniedException;
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
    private TaskDTO taskDTO1;
    private TaskDTO taskDTO2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        taskDTO1 = new TaskDTO(1L, user.getId(), 1L, "Task 1", "Description 1", false);
        taskDTO2 = new TaskDTO(2L, user.getId(), 2L, "Task 2", "Description 2", false);
    }

    @Test
    void testGetTasksByUser() {
        when(taskRepository.findByUserId(1L)).thenReturn(Arrays.asList(
            new Task(taskDTO1.getId(), user, taskDTO1.getTitle(), taskDTO1.getDescription(), taskDTO1.getCompleted(), taskDTO1.getUserTaskId()),
            new Task(taskDTO2.getId(), user, taskDTO2.getTitle(), taskDTO2.getDescription(), taskDTO2.getCompleted(), taskDTO2.getUserTaskId())
        ));

        List<TaskDTO> taskDTOs = taskService.getTasksByUser(1L);

        assertNotNull(taskDTOs);
        assertEquals(2, taskDTOs.size());
        assertEquals(taskDTO1.getId(), taskDTOs.get(0).getId());
        assertEquals(taskDTO2.getId(), taskDTOs.get(1).getId());
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(
            new Task(taskDTO1.getId(), user, taskDTO1.getTitle(), taskDTO1.getDescription(), taskDTO1.getCompleted(), taskDTO1.getUserTaskId())
        ));

        TaskDTO taskDTO = taskService.getTaskById(1L, 1L);

        assertNotNull(taskDTO);
        assertEquals(taskDTO1.getId(), taskDTO.getId());
    }

    @Test
    public void testGetTaskByIdTaskNotFoundException() {
        // Configurar o mock para retornar vazio
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Verificar se a exceção TaskNotFoundException é lançada
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(1L, 1L);
        });
    }

    @Test
    public void testGetTaskByIdAccessDeniedException() {
        // Configurar o mock para retornar uma tarefa
        Task task = new Task();
        User user = new User();
        user.setId(2L); // ID do usuário diferente do userId passado no método
        task.setUser(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Verificar se a exceção AccessDeniedException é lançada
        assertThrows(AccessDeniedException.class, () -> {
            taskService.getTaskById(1L, 1L);
        });
    }

    @Test
    void testSaveTask() {
        Task newTask = new Task(taskDTO1.getId(), user, taskDTO1.getTitle(), taskDTO1.getDescription(), taskDTO1.getCompleted(), taskDTO1.getUserTaskId());
        when(taskRepository.findByUserId(1L)).thenReturn(Arrays.asList(newTask));
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        // Chama o método saveTask e passa uma Task, não TaskDTO
        TaskDTO savedTaskDTO = taskService.saveTask(newTask, 1L);

        assertNotNull(savedTaskDTO);
        assertEquals(taskDTO1.getId(), savedTaskDTO.getId());
    }

    @Test
    public void testSaveTask_RepositoryException() {
        Task task = new Task();
        when(taskRepository.save(task)).thenThrow(new RuntimeException("Erro no repositório"));

        assertThrows(RuntimeException.class, () -> {
            taskService.saveTask(task, 1L);
        });
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(
            new Task(taskDTO1.getId(), user, taskDTO1.getTitle(), taskDTO1.getDescription(), taskDTO1.getCompleted(), taskDTO1.getUserTaskId())
        ));

        // Criando uma instância de Task com base em TaskDTO
        Task updatedTask = new Task(
            taskDTO1.getId(),
            user,
            "Updated Task",  
            taskDTO1.getDescription(),
            taskDTO1.getCompleted(),
            taskDTO1.getUserTaskId()
        );

        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Passando o Task (não TaskDTO) para o método updateTask
        TaskDTO updatedTaskDTO = taskService.updateTask(1L, updatedTask, 1L);

        assertNotNull(updatedTaskDTO);
        assertEquals("Updated Task", updatedTaskDTO.getTitle());
    }

    @Test
    void testUpdateTaskNotFound() {
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(3L, new Task(3L, user, "New Title", "Description", false, 123L), 1L);
        });
    }

    @Test
    void testUpdateTaskAccessDenied() {
        // Criar uma tarefa com um usuário diferente
        Task taskWithDifferentUser = new Task(taskDTO1.getId(), new User(2L), taskDTO1.getTitle(), taskDTO1.getDescription(), taskDTO1.getCompleted(), taskDTO1.getUserTaskId());
        
        // Configurar o mock para retornar a tarefa com um ID diferente (5L)
        when(taskRepository.findById(5L)).thenReturn(Optional.of(taskWithDifferentUser));
        
        // Verificar se a exceção RuntimeException é lançada
        assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(5L, taskWithDifferentUser, 1L);
        });
    }


    @Test
    void testUpdateTaskAccessDeniedException() {
        Task taskWithDifferentUser = new Task(taskDTO1.getId(), new User(2L), taskDTO1.getTitle(), taskDTO1.getDescription(), taskDTO1.getCompleted(), taskDTO1.getUserTaskId());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskWithDifferentUser));
        assertThrows(AccessDeniedException.class, () -> {
            taskService.updateTask(1L, taskWithDifferentUser, 1L);
        });
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(
            new Task(taskDTO1.getId(), user, taskDTO1.getTitle(), taskDTO1.getDescription(), taskDTO1.getCompleted(), taskDTO1.getUserTaskId())
        ));

        taskService.deleteTask(1L, 1L);

        verify(taskRepository, times(1)).delete(any(Task.class));
    }

    @Test
    void testDeleteTaskNotFound() {
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(3L, 1L);
        });
    }

    @Test
    void testDeleteTaskAccessDenied() {
        Task taskWithDifferentUser = new Task(taskDTO1.getId(), new User(2L), taskDTO1.getTitle(), taskDTO1.getDescription(), taskDTO1.getCompleted(), taskDTO1.getUserTaskId());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskWithDifferentUser));
        assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(1L, 1L);
        });
    }

    @Test
    void testDeleteTaskAccessDeniedException() {
        Task taskWithDifferentUser = new Task(taskDTO1.getId(), new User(2L), taskDTO1.getTitle(), taskDTO1.getDescription(), taskDTO1.getCompleted(), taskDTO1.getUserTaskId());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskWithDifferentUser));
        assertThrows(AccessDeniedException.class, () -> {
            taskService.deleteTask(1L, 1L);
        });
    }
}
