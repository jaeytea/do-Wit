package com.dowit.backend.service;

import com.dowit.backend.entity.Task;
import com.dowit.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {


    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
         taskRepository.deleteById(id);
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found."));
    }

    public Task updateTask(Long id, Task updatedTask){
        Task existingTask= getTaskById(id);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setDueDate(updatedTask.getDueDate());


        return existingTask;
    }

    public List<Task> getTaskByStatus(String status){
        return taskRepository.findByStatus(status);
    }
    public List<Task> searchTasks(String title){
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Task> getTaskSort(String field){
        return taskRepository.findAll(
                Sort.by(field)
        );
    }

}
