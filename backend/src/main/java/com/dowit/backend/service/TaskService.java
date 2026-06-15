package com.dowit.backend.service;

import com.dowit.backend.entity.Task;
import com.dowit.backend.entity.Users;
import com.dowit.backend.entity.enums.TaskStatus;
import com.dowit.backend.repository.TaskRepository;
import com.dowit.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    UserRepository userRepository;

    private final TaskRepository taskRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

    }

    //helper method
    private Users getCurrentUser() {

        // JwtAuthFilter already validated the token and stored
        // the user's email in the SecurityContext before this method runs
        // .getName() returns whatever getUsername() returns in Users.java
        // which we set to return email
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // now fetch the full Users object from DB using that email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }


    public List<Task> getAllTasks(){
        Users currentUser = getCurrentUser();
        return taskRepository.findByUser(currentUser);
    }

    public Task createTask(Task task){
        Users currentUser = getCurrentUser();

        task.setUser(currentUser);

        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        Users currentUser = getCurrentUser();
        Task task = taskRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Task not found"));
         taskRepository.delete(task);
    }

    public Task getTaskById(Long id){
        Users currentUser = getCurrentUser();

        return taskRepository.findByIdAndUser(id, currentUser).orElseThrow(() -> new RuntimeException("Task not found."));
    }

    public Task updateTask(Long id, Task updatedTask){
        Task existingTask= getTaskById(id);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setDueDate(updatedTask.getDueDate());


        return taskRepository.save(existingTask);
    }

    public List<Task> getTaskByStatus(TaskStatus status){
        Users currentUser = getCurrentUser();

        return taskRepository.findByUserAndStatus(currentUser, status);
    }
    public List<Task> searchTasks(String title){
        Users currentUser = getCurrentUser();

        return taskRepository.findByUserAndTitleContainingIgnoreCase(currentUser, title);
    }

    public List<Task> getTaskSort(String field){
        Users currentUser = getCurrentUser();

        return taskRepository.findByUser(currentUser,
                Sort.by(field)
        );
    }

}
