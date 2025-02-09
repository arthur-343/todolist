package com.todolist.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.todolist.model.Task;
import com.todolist.model.User;

@SpringBootTest
@ActiveProfiles("test")
class UserTaskIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Task task1;
    private Task task2;

    @BeforeEach
    @Transactional
    void setUp() throws Exception {
        userRepository.deleteAll();
        taskRepository.deleteAll();

        user = new User("username", "user@example.com", "password123");
        
        // Criando as tarefas
        task1 = new Task(null, user, "Task 1", "Description 1", false, 1L);
        task2 = new Task(null, user, "Task 2", "Description 2", false, 2L);

        // Adicionando as tasks manualmente à lista do usuário
        user.getTasks().add(task1);
        user.getTasks().add(task2);

        // Salvando o usuário primeiro, pois as tasks dependem dele
        user = userRepository.save(user);
        
        // Salvando as tasks depois que o usuário já foi salvo
        taskRepository.save(task1);
        taskRepository.save(task2);
        
        taskRepository.flush();
        userRepository.flush();

        // Verificação
        System.out.println("Total tasks no banco: " + taskRepository.findAll().size());
        System.out.println("Tasks associadas ao usuário: " + user.getTasks().size());
    }


    @Test
    @Transactional
    void testUserTasksIntegration() {
        User foundUser = userRepository.findByEmail("user@example.com");

        assertNotNull(foundUser);
        System.out.println("Found User: " + foundUser);
        System.out.println("Tasks antes da inicialização: " + foundUser.getTasks());

        // FORÇANDO a inicialização da lista
        Hibernate.initialize(foundUser.getTasks());

        System.out.println("Tasks depois da inicialização: " + foundUser.getTasks());

        assertEquals(2, foundUser.getTasks().size());
    }
}
