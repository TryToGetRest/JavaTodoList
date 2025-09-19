package Exceptions;

public class TaskAlreadyExistsException extends Exception {
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
