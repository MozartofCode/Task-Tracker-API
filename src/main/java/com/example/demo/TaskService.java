package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    public TaskService(TaskRepository taskRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        task.setStatus("TODO");
        Task saved = taskRepository.save(task);
        notificationService.notifyTaskCreated(saved);
        return saved;
    }

    public Task updateTask(Long id, Task updated) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(updated.getTitle());
        task.setDescription(updated.getDescription());
        task.setStatus(updated.getStatus());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}