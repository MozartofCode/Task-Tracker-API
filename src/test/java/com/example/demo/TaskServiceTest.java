package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_shouldSetStatusToTodo() {
        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Test description");

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        assertEquals("TODO", result.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }
}