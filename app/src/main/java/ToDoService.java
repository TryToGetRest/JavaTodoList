import Exceptions.NotFoundException;
import Exceptions.TaskAlreadyExistsException;

import java.time.LocalDate;
import java.util.ArrayList;

public class ToDoService {
    private TaskRepository taskRepository;
    public ToDoService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public static Command getCommand(String command) throws NotFoundException {
        Command foundedCommand = null;
        for (Command commands : Command.values()) {
            if (command.equalsIgnoreCase(commands.name())) {
                foundedCommand = commands;
            }
        }
        if (foundedCommand == null) throw new NotFoundException("Вы ввели неизвестную команду: " + command);
        return foundedCommand;
    }

    public static TaskStatus getTaskStatus(String taskStatus) throws NotFoundException {
        TaskStatus foundedStatus = null;
        for (TaskStatus status : TaskStatus.values()) {
            if (taskStatus.equalsIgnoreCase(status.name())) {
                foundedStatus = status;
            }
        }
        if (foundedStatus == null) throw new NotFoundException("Такого статуса для задач нет: " + taskStatus);
        return foundedStatus;
    }

    public String addTask(String taskName, String taskBody, LocalDate deadline) throws  TaskAlreadyExistsException{
        if (taskName == null || taskName.isBlank()) {
            return "Название задачи не может быть пустым";
        }
        if(taskRepository.findTask(taskName) != null) {
            throw new TaskAlreadyExistsException("Такая задача уже есть");
        }
        Task task = new Task(taskName);
        task.setTaskBody(taskBody);
        task.setDeadline(deadline);
        task.setTaskStatus(TaskStatus.TODO);
        taskRepository.addTask(task);
        return "Задача успешно добавлена";
    }

    public String deleteTask(String taskNameForDelete) throws NotFoundException{
        if (taskNameForDelete == null) {
            return "Название задачи не может быть пустым";
        }
        if (taskRepository.findTask(taskNameForDelete) == null) {
            throw new NotFoundException("Такой задачи нет");
        }
        Task taskForDelete = taskRepository.findTask(taskNameForDelete);
        taskRepository.deleteTask(taskForDelete);
        return "Задача удалена";
    }

    public void getAllTasks() {
        printTasks(new ArrayList<>(taskRepository.getAllTask()));
    }

    public String editTask(String oldTaskName, String newTaskName, String newTaskBody, TaskStatus newTaskStatus, LocalDate newDeadline) {
        Task task = taskRepository.findTask(oldTaskName);
        if (task == null) {
            return "Задача '" + oldTaskName + "' не найдена";
        }
        if (newTaskName != null && !newTaskName.isBlank()) {
            task.setTaskName(newTaskName);
        }
        if (newTaskBody != null) {
            task.setTaskBody(newTaskBody);
        }
        if (newDeadline != null) {
            task.setDeadline(newDeadline);
        }
        if (newTaskStatus != null) {
            task.setTaskStatus(newTaskStatus);
        }
        return "Задача '" + oldTaskName + "' изменена";
    }

    public ArrayList<Task> filterTasksByStatus(String taskStatusForFilter) throws NotFoundException {
      return taskRepository.filterTasks(getTaskStatus(taskStatusForFilter));
    }

    public ArrayList<Task> sortTasks(String typeOfSort) throws NotFoundException {
        switch (typeOfSort) {
            case "срок":
                return taskRepository.sortedTasksByDeadline();
            case "статус":
                return taskRepository.sortedTasksByTaskStatus();
            default: throw new NotFoundException("Такого варианта сортировки нет");
        }
    }

    public void exitProgram() {
        System.out.println("Завершение программы");
        System.exit(0);
    }

    public void printTasks(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        int index = 1;
        for (Task task : tasks) {
            System.out.printf("%d. Название: %s\n", index++, task.getTaskName());
            System.out.printf("   Описание: %s\n", task.getTaskBody());
            System.out.printf("   Статус: %s\n", task.getTaskStatus());
            System.out.printf("   Дедлайн: %s\n", task.getDeadline());
            System.out.println("------------------------------------");
        }
    }

}

