package ru.borzenkov.todolist.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String taskId) {
        super(String.format("Задача с ID '%s' не найдена", taskId));
    }
}
