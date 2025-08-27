package ru.borzenkov.todolist;

import ru.borzenkov.todolist.controller.TodoController;
import ru.borzenkov.todolist.repository.InMemoryTaskRepository;
import ru.borzenkov.todolist.service.TaskServiceImpl;

public class TodoApplication {
    public static void main(String[] args) {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        TaskServiceImpl service = new TaskServiceImpl(repository);
        TodoController controller = new TodoController(service);
        
        controller.start();
    }
}
