package ru.borzenkov.todolist.repository;

import ru.borzenkov.todolist.model.Task;
import ru.borzenkov.todolist.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(String id);
    List<Task> findAll();
    void deleteById(String id);
    List<Task> findByStatus(TaskStatus status);
}
