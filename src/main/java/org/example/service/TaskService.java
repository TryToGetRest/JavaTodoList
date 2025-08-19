package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.TaskNotFoundException;
import org.example.model.StatusEnum;
import org.example.model.Task;
import org.example.repository.TaskRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final DateTimeFormatter dateFormatter;

    public void addTask(String name, String description, String status, String deadline) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Task task = new Task();

        task.setName(name);
        task.setDescription(description);
        task.setStatus(StatusEnum.valueOf(status.toUpperCase()));
        task.setDeadLine(LocalDate.parse(deadline, dateFormatter));
        taskRepository.add(task);
    }

    public HashMap<Long, Task> getAll() {
        return taskRepository.getAll();
    }

    public Task deleteTask(Long id) throws TaskNotFoundException {
        Task deletedTask = taskRepository.delete(id);
        if (deletedTask == null) throw new TaskNotFoundException(id);
        return deletedTask;
    }

    public Task editTask(Long id,
                        String name,
                        String description,
                        String status,
                        String deadline
                        ) throws TaskNotFoundException {
        Task processingTask = taskRepository.get(id);

        if (processingTask == null) throw new TaskNotFoundException(id);

        if (!name.isEmpty()) processingTask.setName(name);
        if (!description.isEmpty()) processingTask.setDescription(description);
        if (!status.isEmpty()) processingTask.setStatus(StatusEnum.valueOf(status));
        if (!deadline.isEmpty()) processingTask.setDeadLine(LocalDate.parse(deadline, dateFormatter));

        return taskRepository.edit(id, processingTask);
    }

    public HashMap<Long, Task> filter(StatusEnum status) {
        HashMap<Long, Task> allTasks = taskRepository.getAll();
        return (HashMap<Long, Task>) allTasks
                        .entrySet()
                        .stream()
                        .filter(
                        x -> x.getValue()
                                .getStatus()
                                .equals(status)
                        ).collect(
                                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                        );
    }

    public LinkedHashMap<Long, Task> sort() {
        return taskRepository.getAll()
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.comparing(Task::getDeadLine)))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ));
    }
}