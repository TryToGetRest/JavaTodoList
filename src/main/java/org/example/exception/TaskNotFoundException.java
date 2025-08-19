package org.example.exception;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(Long id) {
        super(String.format("Задачи с ID: %d не существует", id));
    }
}