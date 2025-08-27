package ru.borzenkov.todolist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.borzenkov.todolist.exception.InvalidTaskDataException;
import ru.borzenkov.todolist.exception.TaskNotFoundException;
import ru.borzenkov.todolist.model.Task;
import ru.borzenkov.todolist.model.TaskStatus;
import ru.borzenkov.todolist.repository.TaskRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    void addTask_WithValidData_ShouldReturnTask() {
        String title = "Test Task";
        String description = "Test Description";
        LocalDate dueDate = LocalDate.now().plusDays(1);
        Task expectedTask = new Task(title, description, dueDate);
        
        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

        Task result = taskService.addTask(title, description, dueDate);

        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(description, result.getDescription());
        assertEquals(dueDate, result.getDueDate());
        assertEquals(TaskStatus.TODO, result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void addTask_WithEmptyTitle_ShouldThrowException() {
        String title = "";
        String description = "Test Description";
        LocalDate dueDate = LocalDate.now().plusDays(1);

        assertThrows(InvalidTaskDataException.class, () -> {
            taskService.addTask(title, description, dueDate);
        });
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void addTask_WithNullDueDate_ShouldThrowException() {
        String title = "Test Task";
        String description = "Test Description";
        LocalDate dueDate = null;

        assertThrows(InvalidTaskDataException.class, () -> {
            taskService.addTask(title, description, dueDate);
        });
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void addTask_WithPastDueDate_ShouldThrowException() {
        String title = "Test Task";
        String description = "Test Description";
        LocalDate dueDate = LocalDate.now().minusDays(1);

        assertThrows(InvalidTaskDataException.class, () -> {
            taskService.addTask(title, description, dueDate);
        });
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        List<Task> expectedTasks = Arrays.asList(
                new Task("Task 1", "Description 1", LocalDate.now().plusDays(1)),
                new Task("Task 2", "Description 2", LocalDate.now().plusDays(2))
        );
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(expectedTasks.size(), result.size());
        assertEquals(expectedTasks, result);
        verify(taskRepository).findAll();
    }

    @Test
    void getTaskById_WithExistingId_ShouldReturnTask() {
        String taskId = "test-id";
        Task expectedTask = new Task("Test Task", "Test Description", LocalDate.now().plusDays(1));
        expectedTask.setId(taskId);
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(expectedTask));

        Task result = taskService.getTaskById(taskId);

        assertEquals(expectedTask, result);
        verify(taskRepository).findById(taskId);
    }

    @Test
    void getTaskById_WithNonExistingId_ShouldThrowException() {
        String taskId = "non-existing-id";
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(taskId);
        });
        verify(taskRepository).findById(taskId);
    }

    @Test
    void updateTask_WithValidData_ShouldUpdateAndReturnTask() {
        String taskId = "test-id";
        Task existingTask = new Task("Old Title", "Old Description", LocalDate.now().plusDays(1));
        existingTask.setId(taskId);
        existingTask.setStatus(TaskStatus.TODO);
        
        String newTitle = "New Title";
        String newDescription = "New Description";
        LocalDate newDueDate = LocalDate.now().plusDays(5);
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task result = taskService.updateTask(taskId, newTitle, newDescription, newDueDate, newStatus);

        assertEquals(newTitle, result.getTitle());
        assertEquals(newDescription, result.getDescription());
        assertEquals(newDueDate, result.getDueDate());
        assertEquals(newStatus, result.getStatus());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void updateTask_WithNonExistingId_ShouldThrowException() {
        String taskId = "non-existing-id";
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(taskId, "New Title", "New Description", LocalDate.now().plusDays(1), TaskStatus.DONE);
        });
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_WithExistingId_ShouldDeleteTask() {
        String taskId = "test-id";
        Task existingTask = new Task("Test Task", "Test Description", LocalDate.now().plusDays(1));
        existingTask.setId(taskId);
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        taskService.deleteTask(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void deleteTask_WithNonExistingId_ShouldThrowException() {
        String taskId = "non-existing-id";
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(taskId);
        });
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).deleteById(anyString());
    }

    @Test
    void filterByStatus_ShouldReturnFilteredTasks() {
        TaskStatus status = TaskStatus.TODO;
        List<Task> expectedTasks = Arrays.asList(
                new Task("Task 1", "Description 1", LocalDate.now().plusDays(1)),
                new Task("Task 2", "Description 2", LocalDate.now().plusDays(2))
        );
        when(taskRepository.findByStatus(status)).thenReturn(expectedTasks);

        List<Task> result = taskService.filterByStatus(status);

        assertEquals(expectedTasks.size(), result.size());
        assertEquals(expectedTasks, result);
        verify(taskRepository).findByStatus(status);
    }

    @Test
    void sortByDueDate_ShouldReturnSortedTasks() {
        Task task1 = new Task("Task 1", "Description 1", LocalDate.now().plusDays(3));
        Task task2 = new Task("Task 2", "Description 2", LocalDate.now().plusDays(1));
        Task task3 = new Task("Task 3", "Description 3", LocalDate.now().plusDays(2));
        
        List<Task> unsortedTasks = Arrays.asList(task1, task2, task3);
        when(taskRepository.findAll()).thenReturn(unsortedTasks);

        List<Task> result = taskService.sortByDueDate();

        assertEquals(3, result.size());
        assertEquals(task2, result.get(0)); // Earliest date
        assertEquals(task3, result.get(1)); // Middle date
        assertEquals(task1, result.get(2)); // Latest date
        verify(taskRepository).findAll();
    }

    @Test
    void sortByStatus_ShouldReturnSortedTasks() {
        Task task1 = new Task("Task 1", "Description 1", LocalDate.now().plusDays(1));
        task1.setStatus(TaskStatus.DONE);
        Task task2 = new Task("Task 2", "Description 2", LocalDate.now().plusDays(2));
        task2.setStatus(TaskStatus.TODO);
        Task task3 = new Task("Task 3", "Description 3", LocalDate.now().plusDays(3));
        task3.setStatus(TaskStatus.IN_PROGRESS);
        
        List<Task> unsortedTasks = Arrays.asList(task1, task2, task3);
        when(taskRepository.findAll()).thenReturn(unsortedTasks);

        List<Task> result = taskService.sortByStatus();

        assertEquals(3, result.size());
        assertEquals(TaskStatus.TODO, result.get(0).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, result.get(1).getStatus());
        assertEquals(TaskStatus.DONE, result.get(2).getStatus());
        verify(taskRepository).findAll();
    }
}
