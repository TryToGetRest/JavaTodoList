package ru.borzenkov.todolist.controller;

import ru.borzenkov.todolist.model.Task;
import ru.borzenkov.todolist.model.TaskStatus;
import ru.borzenkov.todolist.service.TaskService;
import ru.borzenkov.todolist.util.CommonParser;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TodoController {

    private final TaskService taskService;
    private final Scanner scanner;

    public TodoController(TaskService taskService) {
        this.taskService = taskService;
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        System.out.println("""
                === TODO Приложение ===
                Доступные команды:
                add    - добавить задачу
                list   - показать все задачи
                edit   - редактировать задачу
                delete - удалить задачу
                filter - фильтровать по статусу
                sort   - сортировать задачи
                exit   - выйти
                """);
        
        while (true) {
            System.out.print("Введите команду: ");
            String command = scanner.nextLine().trim().toLowerCase();
            
            try {
                switch (command) {
                    case "add" -> this.handleAddTask();
                    case "list" -> this.handleListTasks();
                    case "edit" -> this.handleEditTask();
                    case "delete" -> this.handleDeleteTask();
                    case "filter" -> this.handleFilterTasks();
                    case "sort" -> this.handleSortTasks();
                    case "exit" -> {
                        System.out.println("До свидания!");
                        return;
                    }
                    default -> System.out.println("Неизвестная команда. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
            System.out.println();
        }
    }
    
    private void handleAddTask() {
        System.out.print("Введите название задачи: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Введите описание задачи (необязательно): ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = null;
        }
        
        System.out.print("Введите дату выполнения (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate dueDate = CommonParser.parseDate(dateStr);
        
        Task task = taskService.addTask(title, description, dueDate);
        System.out.println("Задача добавлена с ID: " + task.getId());
    }
    
    private void handleListTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }
        
        System.out.println("Список всех задач:");
        printTasks(tasks);
    }
    
    private void handleEditTask() {
        System.out.print("Введите ID задачи для редактирования: ");
        String id = scanner.nextLine().trim();
        
        Task task = taskService.getTaskById(id);
        System.out.println("Текущая задача:");
        System.out.println(task.toString());
        
        System.out.print("Введите новое название (Enter для пропуска): ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) title = null;
        
        System.out.print("Введите новое описание (Enter для пропуска): ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) description = null;
        
        System.out.print("Введите новую дату выполнения yyyy-MM-dd (Enter для пропуска): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate dueDate = dateStr.isEmpty() ? null : CommonParser.parseDate(dateStr);
        
        System.out.print("Введите новый статус (TODO/IN_PROGRESS/DONE) (Enter для пропуска): ");
        String statusStr = scanner.nextLine().trim();
        TaskStatus status = statusStr.isEmpty() ? null : CommonParser.parseStatus(statusStr);
        
        Task updatedTask = taskService.updateTask(id, title, description, dueDate, status);
        System.out.println("Задача обновлена:");
        System.out.println(updatedTask.toString());
    }
    
    private void handleDeleteTask() {
        System.out.print("Введите ID задачи для удаления: ");
        String id = scanner.nextLine().trim();
        
        taskService.deleteTask(id);
        System.out.println("Задача удалена.");
    }
    
    private void handleFilterTasks() {
        System.out.print("Введите статус для фильтрации (TODO/IN_PROGRESS/DONE): ");
        String statusStr = scanner.nextLine().trim();
        TaskStatus status = CommonParser.parseStatus(statusStr);
        
        List<Task> tasks = taskService.filterByStatus(status);
        if (tasks.isEmpty()) {
            System.out.println("Задачи с таким статусом не найдены.");
            return;
        }
        
        System.out.println("Задачи со статусом " + status.getDisplayName() + ":");
        printTasks(tasks);
    }
    
    private void handleSortTasks() {
        System.out.println("Выберите тип сортировки:");
        System.out.println("1 - по дате выполнения");
        System.out.println("2 - по статусу");
        
        System.out.print("Введите номер: ");
        String choice = scanner.nextLine().trim();
        
        List<Task> sortedTasks;
        switch (choice) {
            case "1" -> {
                sortedTasks = taskService.sortByDueDate();
                System.out.println("Задачи отсортированы по дате выполнения:");
            }
            case "2" -> {
                sortedTasks = taskService.sortByStatus();
                System.out.println("Задачи отсортированы по статусу:");
            }
            default -> {
                System.out.println("Неверный выбор.");
                return;
            }
        }
        
        if (sortedTasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }
        
        this.printTasks(sortedTasks);
    }

    private void printTasks(List<Task> tasks) {
        tasks.stream().map(Task::toString).forEach(System.out::println);
    }
}
