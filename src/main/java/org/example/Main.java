package org.example;

import org.example.controller.TaskController;
import org.example.repository.TaskRepository;
import org.example.service.TaskService;

import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        TaskRepository taskRepository = new TaskRepository();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        TaskService taskService = new TaskService(taskRepository, dateFormatter);
        TaskController taskController = new TaskController(taskService, dateFormatter);

        while (true) taskController.handleCommand();
    }
}