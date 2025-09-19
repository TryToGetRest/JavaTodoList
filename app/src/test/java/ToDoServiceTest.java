import Exceptions.NotFoundException;
import Exceptions.TaskAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToDoServiceTest {
private TaskRepository taskRepository;
private ToDoService toDoService;
private ToDoController toDoController;

@BeforeEach
    void setup() {
    taskRepository = mock(TaskRepository.class);
    toDoService = new ToDoService(taskRepository);
    }

@Test
  public void addTask_Success() throws TaskAlreadyExistsException {
    String taskName = "testTask";
    String taskBody = "doTest";
   LocalDate deadline = LocalDate.parse("2025-09-09", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    when(taskRepository.findTask(taskName)).thenReturn(null);
    doNothing().when(taskRepository).addTask(any());
    Assertions.assertEquals("Задача успешно добавлена", toDoService.addTask(taskName, taskBody, deadline));
    verify(taskRepository, times(1)).addTask(any());
}

@Test
public void addTask_ExistTask() {
    String taskName = "task";
    Task task = new Task(taskName);
    when(taskRepository.findTask("task")).thenReturn(task);
    Assertions.assertThrows(TaskAlreadyExistsException.class,() -> toDoService.addTask("task", null, null));
    verify(taskRepository, never()).addTask(any());
}

@Test
public void addTask_TaskNameIsNull() throws TaskAlreadyExistsException {
    Task task = new Task(null);
    Assertions.assertEquals("Название задачи не может быть пустым",
            toDoService.addTask(null,null,null));
    verify(taskRepository, never()).addTask(any());
}

    @Test
    public void editTask_TaskExists_Success() {
        String oldName = "Old Task";
        Task task = new Task(oldName);

        when(taskRepository.findTask(oldName)).thenReturn(task);

        String updatedName = "Updated Task";
        String updatedBody = "Updated body";
        TaskStatus updatedStatus = TaskStatus.IN_PROGRESS;
        LocalDate updatedDeadline = LocalDate.parse("2025-09-09", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String result = toDoService.editTask(oldName, updatedName, updatedBody, updatedStatus, updatedDeadline);

        Assertions.assertEquals("Задача '" + oldName + "' изменена", result);
        Assertions.assertEquals(updatedName, task.getTaskName());
        Assertions.assertEquals(updatedBody, task.getTaskBody());
        Assertions.assertEquals(updatedStatus, task.getTaskStatus());
        Assertions.assertEquals(updatedDeadline, task.getDeadline());
    }

    @Test
    public void deleteTask_TaskExists_Success() throws Exception {
        String taskName = "TaskToDelete";
        Task task = new Task(taskName);

        when(taskRepository.findTask(taskName)).thenReturn(task);
        doNothing().when(taskRepository).deleteTask(task);

        String result = toDoService.deleteTask(taskName);

        Assertions.assertEquals("Задача удалена", result);
        verify(taskRepository, times(1)).deleteTask(task);
    }
    @Test
public void deleteTask_TaskNotFound() {
    Task task = new Task("task");
    when(taskRepository.findTask(task.getTaskName())).thenReturn(null);
    Assertions.assertThrows(NotFoundException.class, () -> toDoService.deleteTask(task.getTaskName()));
        verify(taskRepository, never()).deleteTask(any());
    }


    @Test
    public void filterTasksByStatus_ReturnsFilteredList() throws NotFoundException {
        TaskStatus status = TaskStatus.TODO;
        Task task1 = new Task("task1");
        task1.setTaskStatus(status);
        Task task2 = new Task("task2");
        List<Task> filteredTasks = new ArrayList<>();
        filteredTasks.add(task1);
        filteredTasks.add(task2);

        when(taskRepository.filterTasks(status)).thenReturn(new ArrayList<>(filteredTasks));
        List<Task> actualTasks = toDoService.filterTasksByStatus("TODO");

        Assertions.assertEquals(filteredTasks.size(), actualTasks.size());
        Assertions.assertIterableEquals(filteredTasks, actualTasks);

        verify(taskRepository, times(1)).filterTasks(status);

    }

    @Test
    public  void filterTestByStatus_StatusNotFound() {
        String invalidStatus = "invalid status";
        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            toDoService.filterTasksByStatus(invalidStatus);
        });
        Assertions.assertTrue(thrown.getMessage().contains("Такого статуса для задач нет"));

    }

    @Test
    public void sortTasks_ByDeadline_Success() throws Exception {
        TaskStatus status = TaskStatus.TODO;
        Task task1 = new Task("task1");
        task1.setTaskStatus(status);
        Task task2 = new Task("task2");
        List<Task> sortedTasks = new ArrayList<>();
        sortedTasks.add(task1);
        sortedTasks.add(task2);

        when(taskRepository.sortedTasksByDeadline()).thenReturn(new ArrayList<>(sortedTasks));

       List actualTasks = toDoService.sortTasks("срок");
        Assertions.assertEquals(sortedTasks.size(), actualTasks.size());
        Assertions.assertIterableEquals(sortedTasks, actualTasks);

        verify(taskRepository, times(1)).sortedTasksByDeadline();
    }

    @Test
    public void sortTasks_SortIsNotFound() {
    String invalidTypeOfSort = "invalidTypeOfSort";
    NotFoundException thrown = Assertions.assertThrows(NotFoundException.class,
            () -> toDoService.sortTasks(invalidTypeOfSort));
    Assertions.assertTrue(thrown.getMessage().contains("Такого варианта сортировки нет"));
    }
}
