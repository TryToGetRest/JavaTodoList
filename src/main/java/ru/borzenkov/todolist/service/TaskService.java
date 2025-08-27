package ru.borzenkov.todolist.service;

import ru.borzenkov.todolist.model.Task;
import ru.borzenkov.todolist.model.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    Task addTask(String title, String description, LocalDate dueDate);
    List<Task> getAllTasks();
    Task getTaskById(String id);
    Task updateTask(String id, String title, String description, LocalDate dueDate, TaskStatus status);
    void deleteTask(String id);
    List<Task> filterByStatus(TaskStatus status);
    List<Task> sortByDueDate();
    List<Task> sortByStatus();
}
