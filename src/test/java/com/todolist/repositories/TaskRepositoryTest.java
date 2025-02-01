package com.todolist.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.todolist.model.Task;
import com.todolist.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringJUnitConfig
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task task1;
    private Task task2;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        user = new User();
        user.setId(1L);

        task1 = new Task();
        task1.setUser(user);
        task1.setTitle("Task 1");
        task1.setCompleted(false);
        task1.setDescription("Description 1");
        task1.setUserTaskId(1L);
        
        task2 = new Task();
        task2.setUser(user);
        task2.setTitle("Task 2");
        task2.setCompleted(false);
        task2.setDescription("Description 2");
        task2.setUserTaskId(1L);

        taskRepository.save(task1);
        taskRepository.save(task2);
    }

    @Test
    void testFindByUserId() {
        List<Task> tasks = taskRepository.findByUserId(1L);
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testSaveTask() {
        Task newTask = new Task();
        User newUser = new User();
        newUser.setId(2L);
        newTask.setUser(newUser);
        newTask.setTitle("Task 3");
        newTask.setCompleted(true);
        newTask.setDescription("Description 3");
        newTask.setUserTaskId(2L);

        Task savedTask = taskRepository.save(newTask);
        Optional<Task> fetchedTask = taskRepository.findById(savedTask.getId());

        assertTrue(fetchedTask.isPresent());
        assertEquals(savedTask.getDescription(), fetchedTask.get().getDescription());
    }

    @Test
    void testDeleteTask() {
        taskRepository.delete(task1);
        Optional<Task> deletedTask = taskRepository.findById(task1.getId());

        assertFalse(deletedTask.isPresent());
    }
}
