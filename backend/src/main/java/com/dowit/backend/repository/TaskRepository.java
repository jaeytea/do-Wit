package com.dowit.backend.repository;

import com.dowit.backend.entity.Task;
import com.dowit.backend.entity.Users;
import com.dowit.backend.entity.enums.TaskStatus;
import jakarta.persistence.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


        // OLD ones you already have — these are UNSAFE (return all users' tasks)
        // we're replacing them with user-scoped versions below
        List<Task> findByStatus(String status);
        List<Task> findByTitleContainingIgnoreCase(String title);

        // NEW: get all tasks for a specific user
        // Spring reads this method name and writes:
        // SELECT * FROM tasks WHERE user_id = ?
        List<Task> findByUser(Users user);

        // NEW: get tasks for a user WITH sorting (e.g. sort by dueDate)
        List<Task> findByUser(Users user, Sort sort);

        // NEW: get tasks for a user WITH pagination
        Page<Task> findByUser(Users user, Pageable pageable);

        // NEW: filter by user AND status together
        // SELECT * FROM tasks WHERE user_id = ? AND status = ?
        List<Task> findByUserAndStatus(Users user, TaskStatus status);

        // NEW: search by title for a specific user only
        // SELECT * FROM tasks WHERE user_id = ? AND title ILIKE '%keyword%'
        List<Task> findByUserAndTitleContainingIgnoreCase(Users user, String title);

        // NEW: safe single task fetch — checks it belongs to this user
        // SELECT * FROM tasks WHERE id = ? AND user_id = ?
        Optional<Task> findByIdAndUser(Long id, Users user);
    }
