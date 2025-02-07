package com.todolist.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.todolist.model.Task;
import com.todolist.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@Rollback(true)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository; 

    private User user;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        // Criando e salvando o usuário
        user = userRepository.save(new User("username", "user@example.com", "password123"));

        // Criando tarefas associadas ao usuário salvo
        task1 = new Task(null, user, "Task 1", "Description 1", false, 1L);
        task2 = new Task(null, user, "Task 2", "Description 2", false, 2L);

        // Salvando as tarefas no repositório
        taskRepository.save(task1);
        taskRepository.save(task2);
    }

    @Test
    void testSaveTask() {
        // Criando um novo usuário com senha válida
        User newUser = userRepository.save(new User("newusername", "newuser@example.com", "securePassword"));
        
        // Criando e salvando uma nova tarefa
        Task newTask = new Task(null, newUser, "Task 3", "Description 3", true, 2L);
        Task savedTask = taskRepository.save(newTask);
        
        // Buscando a tarefa no repositório
        Optional<Task> fetchedTask = taskRepository.findById(savedTask.getId());

        // Verificando se a tarefa foi salva corretamente
        assertTrue(fetchedTask.isPresent());
        assertEquals(savedTask.getDescription(), fetchedTask.get().getDescription());
    }

    @Test
    void findByUserId() {
        List<Task> tasks = taskRepository.findByUserId(user.getId());

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }
}
