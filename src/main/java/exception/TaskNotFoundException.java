package exception;

public class TaskNotFoundException extends TaskException {
    public TaskNotFoundException(Long id) {
        super("Задача с ID " + id + " не найдена");
    }
}