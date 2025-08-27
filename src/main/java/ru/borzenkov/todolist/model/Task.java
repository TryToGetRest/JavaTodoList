package ru.borzenkov.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import static ru.borzenkov.todolist.util.CommonParser.DEFAULT_DATE_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private TaskStatus status;
    
    public Task(String title, String description, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = TaskStatus.TODO;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Название: %s | Описание: %s | Дата: %s | Статус: %s%n",
                id,
                title,
                description != null ? description : "Нет описания",
                dueDate.format(DEFAULT_DATE_FORMAT),
                status.getDisplayName());
    }
}
