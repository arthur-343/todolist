package com.todolist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.model.dto.TaskDTO;
import com.todolist.service.TaskService;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private ObjectMapper objectMapper;
    private User user;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

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
    void testGetTasks() throws Exception {
        List<TaskDTO> taskDTOs = Arrays.asList(
            new TaskDTO(task1.getId(), task1.getUser().getId(), task1.getUserTaskId(), task1.getTitle(), task1.getDescription(), task1.getCompleted()),
            new TaskDTO(task2.getId(), task2.getUser().getId(), task2.getUserTaskId(), task2.getTitle(), task2.getDescription(), task2.getCompleted())
        );

        when(taskService.getTasksByUser(1L)).thenReturn(taskDTOs);

        mockMvc.perform(get("/tasks")
                .principal(() -> "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(task1.getId()))
                .andExpect(jsonPath("$[1].id").value(task2.getId()));
    }

    @Test
    void testGetTaskById() throws Exception {
        TaskDTO taskDTO = new TaskDTO(task1.getId(), task1.getUser().getId(), task1.getUserTaskId(), task1.getTitle(), task1.getDescription(), task1.getCompleted());

        when(taskService.getTaskById(1L, 1L)).thenReturn(taskDTO);

        mockMvc.perform(get("/tasks/1")
                .principal(() -> "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(task1.getId()));
    }

    @Test
    void testCreateTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO(task1.getId(), task1.getUser().getId(), task1.getUserTaskId(), task1.getTitle(), task1.getDescription(), task1.getCompleted());

        when(taskService.saveTask(any(Task.class), eq(1L))).thenReturn(taskDTO);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1))
                .principal(() -> "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task1.getId()));
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskDTO updatedTaskDTO = new TaskDTO(task1.getId(), task1.getUser().getId(), task1.getUserTaskId(), "Updated Task", task1.getDescription(), task1.getCompleted());

        when(taskService.updateTask(eq(1L), any(Task.class), eq(1L))).thenReturn(updatedTaskDTO);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1))
                .principal(() -> "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L, 1L);

        mockMvc.perform(delete("/tasks/1")
                .principal(() -> "user1"))
                .andExpect(status().isNoContent());
    }
}
