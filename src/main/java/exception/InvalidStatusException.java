package exception;

import model.Status;

public class InvalidStatusException extends TaskException {
    public InvalidStatusException(String status) {
        super("Неверный статус: " + status + ". Допустимые значения: " +
                String.join(", ", Status.getNames()));
    }
}