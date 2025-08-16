package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.TaskRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setDueDate(LocalDate.now());
        testTask.setStatus(Status.TODO);
    }

    @Test
    void addTask_ShouldSaveAndReturnTask() {
        when(repository.save(any(Task.class))).thenReturn(testTask);

        Task result = service.addTask("Test Task", "Test Description", LocalDate.now());

        assertNotNull(result);
        assertEquals(testTask.getId(), result.getId());
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        when(repository.findAll()).thenReturn(Collections.singletonList(testTask));

        List<Task> result = service.getAllTasks();

        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateTask_ShouldUpdateExistingTask() {
        when(repository.findById(1L)).thenReturn(testTask);
        when(repository.save(any(Task.class))).thenReturn(testTask);

        Task updated = service.updateTask(1L, "Updated", null, null, Status.IN_PROGRESS);

        assertEquals("Updated", updated.getTitle());
        assertEquals(Status.IN_PROGRESS, updated.getStatus());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(testTask);
    }

    @Test
    void updateTask_ShouldThrowWhenTaskNotFound() {
        when(repository.findById(1L)).thenReturn(null);

        Task result = service.updateTask(1L, "Updated", null, null, null);

        assertNull(result);
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void deleteTask_ShouldReturnTrueWhenTaskExists() {
        when(repository.findById(1L)).thenReturn(testTask);
        doNothing().when(repository).deleteById(1L);

        boolean result = service.deleteTask(1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_ShouldReturnFalseWhenTaskNotExists() {
        when(repository.findById(1L)).thenReturn(null);

        boolean result = service.deleteTask(1L);

        assertFalse(result);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void filterByStatus_ShouldReturnFilteredTasks() {
        Task doneTask = new Task();
        doneTask.setStatus(Status.DONE);
        when(repository.findAll()).thenReturn(Arrays.asList(testTask, doneTask));

        List<Task> result = service.filterByStatus(Status.DONE);

        assertEquals(1, result.size());
        assertEquals(Status.DONE, result.get(0).getStatus());
    }

    @Test
    void sortByDueDate_ShouldReturnSortedTasks() {
        Task earlyTask = new Task();
        earlyTask.setDueDate(LocalDate.now());
        Task lateTask = new Task();
        lateTask.setDueDate(LocalDate.now().plusDays(1));
        when(repository.findAll()).thenReturn(Arrays.asList(lateTask, earlyTask));

        List<Task> result = service.sortByDueDate();

        assertEquals(earlyTask.getDueDate(), result.get(0).getDueDate());
        assertEquals(lateTask.getDueDate(), result.get(1).getDueDate());
    }

    @Test
    void sortByStatus_ShouldReturnSortedTasks() {
        Task doneTask = new Task();
        doneTask.setStatus(Status.DONE);
        Task inProgressTask = new Task();
        inProgressTask.setStatus(Status.IN_PROGRESS);
        Task todoTask = new Task();
        todoTask.setStatus(Status.TODO);
        when(repository.findAll()).thenReturn(Arrays.asList(doneTask, inProgressTask, todoTask));

        List<Task> result = service.sortByStatus();

        assertEquals(Status.TODO, result.get(0).getStatus());
        assertEquals(Status.IN_PROGRESS, result.get(1).getStatus());
        assertEquals(Status.DONE, result.get(2).getStatus());
    }
}