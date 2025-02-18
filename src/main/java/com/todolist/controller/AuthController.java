package com.todolist.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.model.User;
import com.todolist.model.dto.AuthRequestDTO;
import com.todolist.repositories.UserRepository;
import com.todolist.service.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDTO body) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(body.getEmail()));
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
        User user = userOpt.get();

        if (passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.badRequest().body("Invalid email or password");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequestDTO body) {
        if (!body.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }
        if (userRepository.findByEmail(body.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(body.getPassword()));
        newUser.setEmail(body.getEmail());
        newUser.setUsername(body.getUsername());
        userRepository.save(newUser);

        return ResponseEntity.ok("OK");
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateProfile(@RequestBody User updateUser, @AuthenticationPrincipal User loggedUser) {
        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }
        loggedUser.setUsername(updateUser.getUsername());
        loggedUser.setEmail(updateUser.getEmail());
        userRepository.save(loggedUser);
        return ResponseEntity.ok("Perfil atualizado com sucesso!");
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteProfile(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

}
