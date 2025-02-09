package com.todolist.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "task_sequence", allocationSize = 1)
    private Long id;

    private String title;
    private Boolean completed;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(name = "user_task_id")
    private Long userTaskId;

    // Construtor customizado, pois o Lombok não permite criar o user (devido ao uso de @ManyToOne com a anotação @JoinColumn) nos testes
    public Task(Long id, User user, String title, String description, Boolean completed, Long userTaskId) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.userTaskId = userTaskId;
    }
}
