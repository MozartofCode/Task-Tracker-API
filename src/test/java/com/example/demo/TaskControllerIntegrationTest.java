package com.example.demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TaskControllerIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @BeforeAll
    static void startContainer() {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    
    @MockitoBean
    private NotificationService notificationService;


    @MockitoBean
    private S3Service s3Service;

    @Autowired
    private TaskService taskService;

    @Test
    void createTask_shouldSaveToRealDatabase() {
        doNothing().when(notificationService).notifyTaskCreated(any(Task.class));

        Task task = new Task();
        task.setTitle("Integration test task");
        task.setDescription("Testing with real PostgreSQL");

        Task saved = taskService.createTask(task);

        assertNotNull(saved.getId());
        assertEquals("Integration test task", saved.getTitle());
        assertEquals("TODO", saved.getStatus());
    }

    @Test
    void getAllTasks_shouldReturnSavedTasks() {
        doNothing().when(notificationService).notifyTaskCreated(any(Task.class));

        Task task = new Task();
        task.setTitle("Another test task");
        taskService.createTask(task);

        var tasks = taskService.getAllTasks();

        assertFalse(tasks.isEmpty());
    }
}