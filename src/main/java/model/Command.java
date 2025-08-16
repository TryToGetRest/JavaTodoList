package model;

public enum Command {
    ADD("add", "Добавить новую задачу"),
    LIST("list", "Показать все задачи"),
    EDIT("edit", "Редактировать существующую задачу"),
    DELETE("delete", "Удалить задачу"),
    FILTER("filter", "Показать задачи с определённым статусом"),
    SORT("sort", "Отсортировать задачи"),
    HELP("help", "Показать справку"),
    EXIT("exit", "Завершить работу приложения");

    private final String command;
    private final String description;

    Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public static Command fromString(String text) {
        for (Command cmd : Command.values()) {
            if (cmd.command.equalsIgnoreCase(text)) {
                return cmd;
            }
        }
        return null;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}