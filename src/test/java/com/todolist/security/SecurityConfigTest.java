package com.todolist.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    public void contextLoads() {
        assertThat(securityConfig).isNotNull();
    }

    @Test
    public void testPasswordEncoder() {
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        assertThat(passwordEncoder.matches(password, encodedPassword)).isTrue();
    }

    @Test
    public void testLoginAndRegisterEndpoints() throws Exception {
        mockMvc.perform(formLogin("/auth/login").user("user").password("password"))
                .andExpect(status().is4xxClientError()); // Deve falhar porque o usuário não existe

        // Testar o endpoint de registro (isso precisaria de um controlador de registro simulando uma requisição)
        // Aqui verificamos que a URL é acessível
        mockMvc.perform(post("/auth/register").param("email", "user@example.com")
                .param("password", "password").param("username", "user"))
                .andExpect(status().is4xxClientError()); // Dependendo da implementação, pode retornar erro se a lógica não permitir duplicação de emails
    }
}
