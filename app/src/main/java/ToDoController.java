import Exceptions.NotFoundException;
import Exceptions.TaskAlreadyExistsException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ToDoController {
    public static void main(String[] args) {
        TaskRepository taskRepository = new TaskRepository();
        ToDoService toDoService = new ToDoService(taskRepository);
        System.out.println("""
                Введите команду из списка:
                add - Добавить новую задачу
                list - Показать все задачи
                edit - Редактировать существующую задачу
                delete - Удалить задачу
                filter - Показать задачи с определённым статусом
                sort - Отсортировать задачи
                exit - Завершить работу приложения"""
        );
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                String command = scanner.nextLine();
                Command commandForDo = ToDoService.getCommand(command);

                switch (commandForDo) {
                    case ADD ->  {
                        System.out.println("Введите название новой задачи");
                        String taskName = scanner.nextLine().trim();

                        System.out.println("Введите описание для новой задачи");
                        String taskBody = scanner.nextLine().trim();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        System.out.println("Введите дату в формате гггг-мм-дд:");
                        try{
                            String deadlineByString = scanner.nextLine().trim();
                            LocalDate deadline = LocalDate.parse(deadlineByString, formatter);

                            System.out.println(toDoService.addTask(taskName,taskBody,deadline));
                        } catch (DateTimeParseException e) {
                            System.out.println("Неверный формат даты! Используйте гггг-мм-дд.");
                        }

                    }
                    case DELETE -> {
                        System.out.println("Введите название задачи для удаления");
                        String taskNameForDelete = scanner.nextLine().trim();

                        System.out.println(toDoService.deleteTask(taskNameForDelete));
                    }
                    case LIST -> toDoService.getAllTasks();

                    case EDIT -> {
                        System.out.println("Введите название задачи для изменения");
                        String oldTaskName = scanner.nextLine().trim();

                        System.out.println("Введите новое название или нажмите Enter для пропуска:");
                        String newTaskName = scanner.nextLine().trim();
                        if (newTaskName.isBlank()) newTaskName = null;

                        System.out.println("Введите новое описание или нажмите Enter для пропуска:");
                        String newTaskBody = scanner.nextLine().trim();
                        if (newTaskBody.isBlank()) newTaskBody = null;

                        System.out.println("Введите новый дедлайн в формате гггг-мм-дд или нажмите Enter для пропуска:");
                        String newDeadlineStr = scanner.nextLine().trim();
                        LocalDate newDeadline = null;
                        try{
                            if (!newDeadlineStr.isBlank()) {
                                newDeadline = LocalDate.parse(newDeadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Неверный формат даты! Используйте гггг-мм-дд.");
                        }

                        System.out.println("Введите новый статус (TODO, IN_PROGRESS, DONE) или нажмите Enter для пропуска:");
                        String newTaskStatusStr = scanner.nextLine().trim().toUpperCase();
                        TaskStatus newTaskStatus = null;
                        if (!newTaskStatusStr.isBlank()) {
                            newTaskStatus = toDoService.getTaskStatus(newTaskStatusStr);
                        }
                        System.out.println(toDoService.editTask(oldTaskName, newTaskName, newTaskBody, newTaskStatus, newDeadline));
                    }

                    case FILTER -> {
                        System.out.println("Введите статус для фильтрации (TODO, IN_PROGRESS, DONE)");
                        String taskStatusForFilter = scanner.nextLine().trim();
                        toDoService.printTasks(toDoService.filterTasksByStatus(taskStatusForFilter));

                    }

                    case SORT -> {
                        System.out.println("""
                                Как вы хотите сортировать задачи: по сроку выполнения или по статусу?
                                Введите слово срок или статус""");
                        String typeOfSort = scanner.nextLine().toLowerCase().trim();
                        toDoService.printTasks(toDoService.sortTasks(typeOfSort));

                    }
                    case EXIT -> {
                        toDoService.exitProgram();
                    }
                }

            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            } catch (TaskAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
