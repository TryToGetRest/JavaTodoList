package ru.borzenkov.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    TODO("К выполнению"),
    IN_PROGRESS("В процессе"),
    DONE("Завершено");
    
    private final String displayName;
}
