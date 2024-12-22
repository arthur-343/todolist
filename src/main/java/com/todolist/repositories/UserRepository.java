package com.todolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.todolist.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
