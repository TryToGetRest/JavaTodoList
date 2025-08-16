import controller.TaskController;
import repository.TaskRepository;
import service.TaskService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskRepository repository = new TaskRepository();
        TaskService service = new TaskService(repository);
        Scanner scanner = new Scanner(System.in);
        TaskController controller = new TaskController(service, scanner);

        controller.start();
    }
}