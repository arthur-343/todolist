package com.todolist.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "task_sequence", allocationSize = 1)
    private Long id;
    
    private String title;
    private Boolean completed;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(name = "user_task_id")
    private Long userTaskId;

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', description='" + description + "', completed=" + completed + "}";
    }
}
