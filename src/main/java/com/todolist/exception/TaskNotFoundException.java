package com.todolist.exception;

public class TaskNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public TaskNotFoundException(Long id) {
        super("Could not find the task with the id " + id);
    }
    
    public TaskNotFoundException(String message) {
        super(message);
    }
}
