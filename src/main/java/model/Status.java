package model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Status {
    TODO,
    IN_PROGRESS,
    DONE;

    public static List<String> getNames() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
