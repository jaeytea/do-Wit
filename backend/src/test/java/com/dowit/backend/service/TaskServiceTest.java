package com.dowit.backend.service;

import com.dowit.backend.entity.Task;
import com.dowit.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testGetAllTasks() {
        List<Task> tasks = List.of(
                new Task(),
                new Task()
        );

        when(taskRepository.findAll()).thenReturn(tasks);
        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository).findAll();

    }

    @Test
    void testCreateTask() {

        Task task = new Task();
        task.setTitle("Learn Spring");

        when(taskRepository.save(task)).thenReturn(task);
        Task saved = taskService.createTask(task);

        assertEquals(
                "Learn Spring",
                saved.getTitle()
        );

        verify(taskRepository).save(task);
    }

    @Test
    void testGetTaskById() {

        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals(
                1L,
                result.getId()
        );
    }

    @Test
    void testDeleteTask() {

        taskService.deleteTask(1L);

        verify(taskRepository)
                .deleteById(1L);
    }


}
