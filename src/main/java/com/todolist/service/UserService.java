package com.todolist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.todolist.exception.UserNotFoundException;
import com.todolist.model.User;
import com.todolist.model.dto.TaskDTO;
import com.todolist.model.dto.UserDTO;
import com.todolist.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @Transactional  // Garante que a sessão esteja aberta e as tasks sejam carregadas
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail(),
                        taskService.getTasksByUser(user.getId())))  // Carrega as tasks do usuário
                .collect(Collectors.toList());
    }

    @Transactional  // Garante que a sessão esteja aberta e as tasks sejam carregadas
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        List<TaskDTO> taskDTOs = taskService.getTasksByUser(user.getId());
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), taskDTOs);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null) {
            user.setPassword(userDetails.getPassword());
        }
        return userRepository.save(user);
    }
}
