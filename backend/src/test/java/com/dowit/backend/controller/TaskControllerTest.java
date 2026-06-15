package com.dowit.backend.controller;

import com.dowit.backend.entity.Task;
import com.dowit.backend.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void testGetAllTasks()
            throws Exception {

        Task task = new Task();
        task.setTitle("Spring");

        when(taskService.getAllTasks())
                .thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].title")
                                .value("Spring")
                );
    }

    @Test
    void testCreateTask()
            throws Exception {

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Learn");

        when(taskService.createTask(any(Task.class)))
                .thenReturn(task);

        mockMvc.perform(
                        post("/tasks")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content("""
                        {
                          "title":"Learn",
                          "description":"Study",
                          "priority":"HIGH",
                          "status":"PENDING",
                          "dueDate":"2026-06-30"
                        }
                    """)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.title")
                                .value("Learn")
                );
    }
}
