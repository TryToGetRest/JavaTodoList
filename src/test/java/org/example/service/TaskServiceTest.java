package org.example.service;

import org.example.exception.TaskNotFoundException;
import org.example.model.StatusEnum;
import org.example.model.Task;
import org.example.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;

class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;
    TaskService taskService;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository, DateTimeFormatter.ofPattern("dd.MM.yy"));

        task1 = new Task("1",
                "",
                StatusEnum.TODO,
                LocalDate.of(2025, 11, 25)
        );
        task2 = new Task("2",
                "",
                StatusEnum.TODO,
                LocalDate.of(2025, 11, 15)
        );
        task3 = new Task("2",
                "",
                StatusEnum.IN_PROGRESS,
                LocalDate.of(2025, 10, 15)
        );
    }

    @Test
    void addTask() {
        String deadline = task1.getDeadLine().format(DateTimeFormatter.ofPattern("dd.MM.yy"));
        taskService.addTask(
                task1.getName(),
                task1.getDescription(),
                task1.getStatus().toString(),
                deadline
        );
        Mockito.verify(taskRepository).add(task1);
    }

    @Test
    void deleteTask() {
        Long id = 1L;
        Mockito.when(taskRepository.delete(id)).thenReturn(task1);
        Task deletedResult = null;
        try {
            deletedResult = taskService.deleteTask(id);
        } catch (TaskNotFoundException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(task1, deletedResult);
    }

    @Test
    void deleteTask_throwsTaskNotFound() {
        Long id = 888L;
        Mockito.when(taskRepository.delete(id)).thenReturn(null);
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(id));
    }

    @Test
    void editTask() {
        Long id = 1L;
        Mockito.when(taskRepository.get(id)).thenReturn(task1);
        Mockito.when(taskRepository.edit(id, task2)).thenReturn(task2);
        String deadline = task2.getDeadLine().format(DateTimeFormatter.ofPattern("dd.MM.yy"));

        Task editedTask = null;
        try {
            editedTask = taskService.editTask(id,
                    task2.getName(),
                    task2.getDescription(),
                    task2.getStatus().toString(),
                    deadline);
        } catch (TaskNotFoundException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(task2, editedTask);
    }

    @Test
    void editTask_throwsTaskNotFound() {
        Long id = 888L;
        Mockito.when(taskRepository.get(id)).thenReturn(null);
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.editTask(id,
                task2.getName(),
                task2.getDescription(),
                task2.getStatus().toString(),
                task2.getDeadLine().toString())
        );
    }

    @Test
    void filter() {
        HashMap<Long, Task> expectedMap = new HashMap<>();
        expectedMap.put(3L, task3);

        HashMap<Long, Task> processedMap = new HashMap<>();
        processedMap.put(1L, task1);
        processedMap.put(2L, task2);
        processedMap.put(3L, task3);

        Mockito.when(taskRepository.getAll()).thenReturn(processedMap);

        HashMap<Long, Task> resultMap = taskService.filter(StatusEnum.IN_PROGRESS);

        Assertions.assertEquals(expectedMap, resultMap);
    }

    @Test
    void sort() {
        LinkedHashMap<Long, Task> expectedMap = new LinkedHashMap<>();
        expectedMap.put(3L, task3);
        expectedMap.put(2L, task2);
        expectedMap.put(1L, task1);

        HashMap<Long, Task> processedMap = new HashMap<>();
        processedMap.put(1L, task1);
        processedMap.put(2L, task2);
        processedMap.put(3L, task3);

        Mockito.when(taskRepository.getAll()).thenReturn(processedMap);

        LinkedHashMap<Long, Task> resultMap = taskService.sort();
        Assertions.assertEquals(expectedMap, resultMap);
    }
}