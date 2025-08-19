package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.exception.TaskNotFoundException;
import org.example.model.StatusEnum;
import org.example.model.Task;
import org.example.service.TaskService;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskServices;
    private final DateTimeFormatter dateFormatter;
    private final Scanner scanner = new Scanner(System.in);
    private static final int exitCode = 0;

    public void handleCommand() {
        System.out.println("""
            
            add
            list
            edit
            delete
            filter
            sort
            exit
                """);

        String commandLine = scanner.nextLine();
        String[] parts = commandLine.split(" ");
        String command = parts[0];

        try {
            switch (command) {
                case "add" -> addTask();
                case "list" -> list();
                case "edit" -> {
                    Long argument = Long.valueOf(parts[1]);
                    edit(argument);
                }
                case "delete" -> {
                    Long argument = Long.valueOf(parts[1]);
                    delete(argument);
                }
                case "filter" -> {
                    String argument = parts[1];
                    filter(argument);
                }
                case "sort" -> sort();
                case "exit" -> System.exit(exitCode);
                default -> System.out.println("Неизвестная команда");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Обязательно укажите аргумент");
        } catch (NumberFormatException e) {
            System.out.println("ID задачи внатуре должен быть числом");
        }

    }

    private void addTask() {

        System.out.print("Название: ");
        String name = scanner.nextLine();

        System.out.print("Описание (можно пустое): ");
        String description = scanner.nextLine();

        System.out.print("Статус: ");
        String status = scanner.nextLine();

        System.out.print("Дедлайн (dd.mm.yy): ");
        String deadline = scanner.nextLine();

        try {
            taskServices.addTask(
                    name,
                    description,
                    status,
                    deadline
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Введите правильное значение статуса");
        } catch (DateTimeException e) {
            System.out.println("Некорректно введена дата");
        }
    }

    private void list() {
        HashMap<Long, Task> tasks = taskServices.getAll();
        printTasks(tasks);
    }

    private void edit(Long id) {
        System.out.println("Введите новые значения, или оставьте поля пустыми для сохранения старых");

        System.out.print("Название: ");
        String name = scanner.nextLine();

        System.out.print("Описание (можно пустое): ");
        String description = scanner.nextLine();

        System.out.print("Статус: ");
        String status = (scanner.nextLine().toUpperCase());

        System.out.print("Дедлайн (dd.mm.yy): ");
        String deadLine = scanner.nextLine();

        Task editedTask = null;
        try {
            editedTask = taskServices.editTask(id, name, description, status, deadLine);

            HashMap<Long, Task> printMap = new HashMap<>();
            printMap.put(id, editedTask);

            printTasks(printMap);
        } catch (TaskNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Введите правильное значение статуса");
        } catch (DateTimeException e) {
            System.out.println("Некорректно введена дата");
        }
    }

    private void delete(Long id) {
        HashMap<Long, Task> printMap = new HashMap<>();
        Task t = null;
        try {
            t = taskServices.deleteTask(id);
            printMap.put(id, t);
            printTasks(printMap);
        } catch (TaskNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void filter(String argument) {
        StatusEnum status = StatusEnum.valueOf(argument);

        printTasks(taskServices.filter(status));
    }

    private void sort() {
        printTasks(taskServices.sort());
    }

    private void printTasks(HashMap<Long, Task> tasks) {
        System.out.println();
        System.out.printf("%-5s %-15s %-25s %-12s %-12s%n", "ID", "Название", "Описание", "Статус", "Дедлайн");
        System.out.println("-------------------------------------------------------------------------------");

        for (Map.Entry<Long, Task> entry : tasks.entrySet()) {
            Task t = entry.getValue();
            System.out.printf("%-5d %-15s %-25s %-12s %-12s%n",
                    entry.getKey(),
                    t.getName(),
                    t.getDescription(),
                    t.getStatus(),
                    t.getDeadLine().format(dateFormatter));
        }
    }
}