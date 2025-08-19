package org.example.repository;

import org.example.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    private final Map<Long, Task> repo = new HashMap<>();
    private Long id = 1L;
    public void add(Task task) {
        repo.put(id, task);
        id++;
    }

    public Task get(Long id) {
        return repo.get(id);
    }

    public Task edit(Long id, Task task) {
        return repo.put(id, task);
    }

    public Task delete(Long id) {
        return repo.remove(id);
    }

    public HashMap<Long, Task> getAll() {
        return new HashMap<>(repo);
    }
}