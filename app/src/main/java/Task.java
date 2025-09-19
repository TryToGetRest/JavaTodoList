
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter
@ToString
public class Task {
    private String taskName;
    private String taskBody;
    private TaskStatus taskStatus;
    private LocalDate deadline;

    Task(String taskName){
        this.taskName = taskName;
        this.taskBody = null;
        this.deadline = null;
        this.taskStatus = TaskStatus.TODO;
    }

}
