package com.todolist.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.todolist.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "tasks")  // Indica que as tasks devem ser carregadas junto com o usuário
    User findByEmail(String email);
}

