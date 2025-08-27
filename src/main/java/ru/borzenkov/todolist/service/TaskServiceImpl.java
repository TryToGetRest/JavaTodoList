package ru.borzenkov.todolist.service;

import ru.borzenkov.todolist.exception.InvalidTaskDataException;
import ru.borzenkov.todolist.exception.TaskNotFoundException;
import ru.borzenkov.todolist.model.Task;
import ru.borzenkov.todolist.model.TaskStatus;
import ru.borzenkov.todolist.repository.TaskRepository;
import ru.borzenkov.todolist.util.StringUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    @Override
    public Task addTask(String title, String description, LocalDate dueDate) {
        this.validateTaskData(title, description, dueDate);
        
        Task task = new Task(title, description, dueDate);
        return taskRepository.save(task);
    }
    
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    @Override
    public Task getTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
    
    @Override
    public Task updateTask(String id, String title, String description, LocalDate dueDate, TaskStatus status) {
        Task task = this.getTaskById(id);
        
        if (!StringUtils.isBlank(title)) {
            task.setTitle(title);
        }
        if (!StringUtils.isBlank(description)) {
            task.setDescription(description);
        }
        if (dueDate != null) {
            task.setDueDate(dueDate);
        }
        if (status != null) {
            task.setStatus(status);
        }
        
        return taskRepository.save(task);
    }
    
    @Override
    public void deleteTask(String id) {
        this.getTaskById(id);
        taskRepository.deleteById(id);
    }
    
    @Override
    public List<Task> filterByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    // Плохая реализация сортировок...
    // Лучше делать на уровне запроса к базе, но тут тестовое задание на java core
    @Override
    public List<Task> sortByDueDate() {
        return taskRepository.findAll().stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .toList();
    }
    
    @Override
    public List<Task> sortByStatus() {
        return taskRepository.findAll().stream()
                .sorted(Comparator.comparing(Task::getStatus))
                .toList();
    }
    
    private void validateTaskData(String title, String description, LocalDate dueDate) {
        if (StringUtils.isBlank(title)) {
            throw new InvalidTaskDataException("Название задачи не может быть пустым");
        }
        if (StringUtils.isBlank(description)) {
            throw new InvalidTaskDataException("Описание задачи не может быть пустым");
        }
        if (dueDate == null) {
            throw new InvalidTaskDataException("Дата выполнения не может быть пустой");
        }
        if (dueDate.isBefore(LocalDate.now())) {
            throw new InvalidTaskDataException("Дата выполнения не может быть в прошлом");
        }
    }
}

