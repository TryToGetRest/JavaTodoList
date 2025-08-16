package exception;

public class InvalidDateException extends TaskException {
    public InvalidDateException(String date) {
        super("Неверный формат даты: " + date + ". Ожидается формат дд.мм.гггг");
    }
}