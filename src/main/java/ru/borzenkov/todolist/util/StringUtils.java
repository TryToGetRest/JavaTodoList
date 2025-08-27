package ru.borzenkov.todolist.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
