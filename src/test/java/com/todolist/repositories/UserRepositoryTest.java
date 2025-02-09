package com.todolist.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.todolist.model.User;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    @Transactional
    void setUp() throws Exception {
        userRepository.deleteAll();

        user = new User("username", "user@example.com", "password123");
        user = userRepository.save(user);
    }

    @Test
    @Transactional
    void testSaveUser() {
        User foundUser = userRepository.findByEmail("user@example.com");

        assertNotNull(foundUser);
        assertEquals("username", foundUser.getUsername());
        assertEquals("user@example.com", foundUser.getEmail());
        assertEquals("password123", foundUser.getPassword());
    }
}
