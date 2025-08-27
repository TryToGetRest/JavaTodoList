package ru.borzenkov.todolist.repository;

import ru.borzenkov.todolist.model.Task;
import ru.borzenkov.todolist.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTaskRepository implements TaskRepository {

    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    
    @Override
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }
    
    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }
    
    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }
    
    @Override
    public void deleteById(String id) {
        tasks.remove(id);
    }
    
    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return tasks.values().stream()
                .filter(task -> Objects.equals(task.getStatus(), status))
                .toList();
    }
}
