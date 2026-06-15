package com.dowit.backend.controller;

import com.dowit.backend.entity.Task;
import com.dowit.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks(){
        return taskService.getAllTasks();

    }

    @PostMapping
    public Task createTask(@Valid @RequestBody Task task){
        return taskService.createTask(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask){
        return taskService.updateTask(id, updatedTask);
    }

    @GetMapping("/search")
    public List<Task> searchTasks(
            @RequestParam String title){
        return taskService.searchTasks(title);
    }

    @GetMapping("/status")
    public List<Task> getTasksByStatus(
            @RequestParam String status){
        return taskService.getTaskByStatus(status);
    }

    @GetMapping("/sorted")
    public List<Task> getSortedTasks(
            @RequestParam String field){
        return taskService.getTaskSort(field);
    }
}
