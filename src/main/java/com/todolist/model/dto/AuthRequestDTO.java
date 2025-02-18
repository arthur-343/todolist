package com.todolist.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestDTO {
    private String email;
    private String password;
    private String username; // Campo opcional, usado apenas no registro
}
