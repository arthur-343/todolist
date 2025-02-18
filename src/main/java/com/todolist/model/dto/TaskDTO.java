package com.todolist.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;
    private Long userId;
    private Long userTaskId;
    private String title;
    private String description;
    private Boolean completed;
    
    public TaskDTO(Long userTaskId, String title, String description, Boolean completed) {
        this.userTaskId = userTaskId;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }
}
