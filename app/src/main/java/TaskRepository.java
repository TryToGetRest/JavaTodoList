import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import lombok.Getter;
@Getter
public class TaskRepository {
        private ArrayList<Task> tasks;

        TaskRepository() {
            tasks = new ArrayList<>();
        }

        void addTask(Task newTask) {
            tasks.add(newTask);
        }
        void deleteTask(Task task)  {
            tasks.remove(task);
        }
        ArrayList<Task> getAllTask() {
            return  tasks;
        }

        Task findTask(String taskName) {
            for (Task task: tasks) {
                if (task.getTaskName().equals(taskName))
                    return task;
            }
            return null;
        }

        ArrayList<Task> filterTasks(TaskStatus taskStatus) {
            return  tasks.stream().filter(task -> task.getTaskStatus() == taskStatus).collect(Collectors.toCollection(ArrayList::new));
        }

        ArrayList<Task> sortedTasksByDeadline() {
            return  tasks.stream().sorted(Comparator.comparing(Task::getDeadline)).collect(Collectors.toCollection(ArrayList::new));
        }
        ArrayList<Task> sortedTasksByTaskStatus() {
            return tasks.stream().sorted(Comparator.comparing(Task::getTaskStatus)).collect(Collectors.toCollection(ArrayList::new));
        }
    }


