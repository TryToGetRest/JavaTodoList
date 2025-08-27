package ru.borzenkov.todolist.util;

import lombok.experimental.UtilityClass;
import ru.borzenkov.todolist.exception.InvalidTaskDataException;
import ru.borzenkov.todolist.model.TaskStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
public class CommonParser {

    public static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ISO_DATE;

    public LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DEFAULT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidTaskDataException("Неверный формат даты. Используйте формат yyyy-MM-dd");
        }
    }

    public TaskStatus parseStatus(String statusStr) {
        try {
            return TaskStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTaskDataException("Неверный статус. Используйте TODO, IN_PROGRESS или DONE");
        }
    }
}
