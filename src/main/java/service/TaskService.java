package service;

import lombok.RequiredArgsConstructor;
import model.Status;
import model.Task;
import repository.TaskRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;

    public Task addTask(String title, String description, LocalDate dueDate) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setStatus(Status.TODO);
        return repository.save(task);
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task updateTask(Long id, String title, String description, LocalDate dueDate, Status status) {
        Task task = repository.findById(id);
        if (task == null) {
            return null;
        }

        if (title != null) task.setTitle(title);
        if (description != null) task.setDescription(description);
        if (dueDate != null) task.setDueDate(dueDate);
        if (status != null) task.setStatus(status);

        return repository.save(task);
    }

    public boolean deleteTask(Long id) {
        if (repository.findById(id) == null) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public List<Task> filterByStatus(Status status) {
        return repository.findAll().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> sortByDueDate() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    public List<Task> sortByStatus() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Task::getStatus))
                .collect(Collectors.toList());
    }
}
